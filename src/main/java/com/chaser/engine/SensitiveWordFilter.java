package com.chaser.engine;

import com.chaser.model.Node;
import com.chaser.model.SensitiveWord;
import com.chaser.plugin.Fragment;
import com.chaser.plugin.Processor;
import com.chaser.plugin.Resource;
import com.chaser.utils.Converter;

import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * description:敏感词过滤器
 *
 * @author wangyj on 2018/4/25
 */
public class SensitiveWordFilter {
    /**
     * AC自动机的根结点，根结点不存储任何字符信息
     * 65536 是unicode最大值 + 1 代表根节点
     */
    private Node root = new Node(65536);
    /**
     * 敏感词最短长度
     */
    private int minLen = 0;
    /**
     * 停顿词set
     */
    private Set<Integer> stopWordSet;
    /**
     * 敏感词资源文件类
     */
    private Resource sensitiveWordResource;
    /**
     * 结束词/无意义的词资源文件类
     */
    private Resource stopWordResource;
    /**
     * 数据处理类
     */
    private DataInit dataInit = new DataInit();
    /**
     * 忽略关键词 做占位
     */
    private IgnoreFinder ignoreFinder = new IgnoreFinder();


    public SensitiveWordFilter() {
        this(null, null);
    }

    public SensitiveWordFilter(Resource sensitiveWordResource) {
        this(sensitiveWordResource, null);
    }

    public SensitiveWordFilter(Resource sensitiveWordResource, Resource stopWordResource) {
        if (sensitiveWordResource == null) {
            sensitiveWordResource = new FileResource("word.txt");
        }
        if (stopWordResource == null) {
            stopWordResource = new FileResource("stop.txt");
        }
        this.sensitiveWordResource = sensitiveWordResource;
        this.stopWordResource = stopWordResource;
        reload();
    }

    /**
     * <p>功能描述: 初始化引擎</p>
     * <p>创建人: wangyj </p>
     * <p>创建日期: 2018/4/25 17:56 </p>
     */
    public void reload() {
        minLen = dataInit.generalTree(sensitiveWordResource.readWords());
        stopWordSet = dataInit.generalStopWords(stopWordResource.readWords());
    }

    /**
     * <p>功能描述: 检测是否存在敏感词</p>
     * <p>创建人: wangyj </p>
     * <p>创建日期: 2018/4/26 12:20 </p>
     *
     * @param text 待分析的文本
     * @return 返回是否存在敏感词
     */
    public boolean hasSensitiveWord(String text) {
        return (boolean) match(ignoreFinder, text, null, ModeType.HAS_WORD);
    }

    /**
     * <p>功能描述: 返回敏感词集合</p>
     * <p>创建人: wangyj </p>
     * <p>创建日期: 2018/4/26 12:27 </p>
     *
     * @param text 待分析的文本
     * @return 返回敏感词集合
     */
    @SuppressWarnings("unchecked")
    public List<SensitiveWord> getSensitiveWordList(String text) {
        return (List<SensitiveWord>) match(new WordFinder(), text, null, ModeType.GET_WORDS);
    }

    /**
     * <p>功能描述: 返回过滤后的文本</p>
     * <p>创建人: wangyj </p>
     * <p>创建日期: 2018/4/26 12:27 </p>
     *
     * @param text 待分析的文本
     * @param pre  敏感词包裹前缀
     * @param last 敏感词包裹后缀
     * @return 返回过滤后的文字
     */
    public String highlightFilter(String text, String pre, String last) {
        return (String) match(ignoreFinder, text, new HtmlFragment(pre, last), ModeType.DO_FILTER);
    }

    /**
     * <p>功能描述: 返回过滤后的文本</p>
     * <p>创建人: wangyj </p>
     * <p>创建日期: 2018/4/26 12:27 </p>
     *
     * @param text 待分析的文本
     * @param sign 替换的文字
     * @return 返回过滤后的文字
     */
    public String replaceFilter(String text, String sign) {
        return (String) match(ignoreFinder, text, new ReplaceFragment(sign), ModeType.DO_FILTER);
    }

    /**
     * <p>功能描述: 自定义获取敏感词</p>
     * <p>创建人: wangyj </p>
     * <p>创建日期: 2018/4/26 12:36 </p>
     *
     * @param processor 用户自己实现的敏感词处理器
     * @param text      待分析的文本
     * @return 敏感词集合
     */
    public Object customGetWords(Processor processor, String text) {
        return match(processor, text, null, ModeType.GET_WORDS);
    }

    /**
     * <p>功能描述: 自定义解析文本</p>
     * <p>创建人: wangyj </p>
     * <p>创建日期: 2018/4/26 12:36 </p>
     *
     * @param fragment 用户自己实现的敏感词替换器
     * @param text     待分析的文本
     * @return 处理后的文本
     */
    public String customDoFilter(Fragment fragment, String text) {
        return (String) match(ignoreFinder, text, fragment, ModeType.DO_FILTER);
    }

    /**
     * <p>功能描述: 根据解析器、替换器 和 解析模式 解析文本，并返回结果值</p>
     * <p>创建人: wangyj </p>
     * <p>创建日期: 2018/4/26 11:23 </p>
     *
     * @param processor 敏感词解析器
     * @param text      待分析的文本
     * @param fragment  敏感词替换策略
     * @param modeType  解析模式
     */
    private Object match(Processor processor, String text, Fragment fragment, ModeType modeType) {
        if (root != null || !"".equals(text.trim()) || text.length() >= minLen) {
            char[] chars = text.toCharArray();
            int length = chars.length;
            Node tempRoot = root;
            Node currentNode = tempRoot;
            int begin = 0;
            StringBuilder builder = new StringBuilder();
            int i = 0;
            while (i < length) {
                int currentChar = Converter.up2lowAndFull2half(chars[i]);
                // 如果是停顿词跳过
                if (stopWordSet != null && stopWordSet.contains(currentChar)) {
                    continue;
                }
                // 文本串中的字符和AC自动机中的字符进行比较
                if (currentNode.getTable().get(currentChar) != null) {
                    currentNode = currentNode.getTable().get(currentChar);
                    // 这里很容易被忽视，因为一个目标串的中间某部分字符串可能正好包含另一个目标字符串，
                    // 即使当前结点不表示一个目标字符串的终点，但到当前结点为止可能恰好包含了一个字符串
                    if (currentNode.getFail() != null && currentNode.getFail().isWord()) {
                        if (ModeType.GET_WORDS.equals(modeType)) {
                            processor.process(currentNode.getFail().getWord(), i);
                        } else if (ModeType.HAS_WORD.equals(modeType)) {
                            return true;
                        }
                    }
                    // 若相等，自动机进入下一状态
                    if (currentNode.isWord()) {
                        if (ModeType.DO_FILTER.equals(modeType)) {
                            String formatWord = fragment.format(currentNode.getWord());
                            builder.append(formatWord);
                        } else if (ModeType.GET_WORDS.equals(modeType)) {
                            processor.process(currentNode.getWord(), i);
                        } else if (ModeType.HAS_WORD.equals(modeType)) {
                            return true;
                        }
                        begin = i + 1;
                    }
                    i++;
                } else {
                    // 若不等，找到下一个应该比较的状态
                    currentNode = currentNode.getFail();
                    // 到根结点还未找到，说明文本串中以ch作为结束的字符片段不是任何目标字符串的前缀，状态机重置，比较下一个字符
                    if (currentNode == null) {
                        currentNode = tempRoot;
                        i++;
                    }
                    if (currentNode == null || currentNode.getValue() == 65536) {
                        if (ModeType.DO_FILTER.equals(modeType)) {
                            for (; begin < i; begin++) {
                                builder.append(chars[begin]);
                            }
                        }
                        begin = i;
                    }
                }
            }
            if (ModeType.HAS_WORD.equals(modeType)) {
                return false;
            } else if (ModeType.GET_WORDS.equals(modeType)) {
                return processor.getReturnValue();
            } else if (ModeType.DO_FILTER.equals(modeType)) {
                if (begin != text.length()) {
                    for (; begin < text.length(); begin++) {
                        builder.append(chars[begin]);
                    }
                }
            }
            return builder.toString();
        }
        return text;
    }

    private class DataInit {
        /**
         * 构造、生成词库树。并返回所有敏感词中最短的词的长度。
         *
         * @param sensitiveWords 词库
         * @return 返回所有敏感词中最短的词的长度。
         */
        private int generalTree(List<String> sensitiveWords) {
            if (sensitiveWords == null || sensitiveWords.isEmpty()) {
                root = new Node(65536);
                return 0;
            }
            Node rooTemp = new Node(65536);
            int len = 0;
            for (String word : sensitiveWords) {
                if (len == 0) {
                    len = word.length();
                } else if (word.length() < len) {
                    len = word.length();
                }
                // 构建树
                Node curr = rooTemp;
                for (int i = 0; i < word.length(); i++) {
                    int ch = Converter.up2lowAndFull2half(word.charAt(i));
                    Node temp = curr.getTable().get(ch);
                    if (temp == null) {
                        temp = new Node(ch);
                        curr.getTable().put(ch, temp);
                    }
                    curr = temp;
                }
                // 最后一个节点赋值字符串
                curr.setWord(word);
            }
            // 广度优先遍历所使用的队列
            Queue<Node> queue = new LinkedBlockingDeque<>();
            // 单独处理根结点的所有孩子结点
            for (Node node : rooTemp.getTable().values()) {
                if (node != null) {
                    // 根结点的所有孩子结点的fail都指向根结点
                    node.setFail(rooTemp);
                    queue.add(node);
                }
            }
            while (!queue.isEmpty()) {
                // 确定出列结点的所有孩子结点的fail的指向
                Node p = queue.remove();
                for (Node node : p.getTable().values()) {
                    queue.add(node);
                    Node failTo = p.getFail();
                    while (true) {
                        if (failTo == null) {
                            node.setFail(rooTemp);
                            break;
                        } else if (failTo.getTable().get(node.getValue()) != null) {
                            node.setFail(failTo.getTable().get(node.getValue()));
                            break;
                        } else {
                            failTo = failTo.getFail();
                        }
                    }
                }
            }
            root = rooTemp;
            return len;
        }

        private Set<Integer> generalStopWords(List<String> stopWordList) {
            if (stopWordList == null || stopWordList.isEmpty()) {
                return new HashSet<>();
            }
            Set<Integer> stopwdSet = new HashSet<>();
            for (String curr : stopWordList) {
                char[] chs = curr.toCharArray();
                for (char c : chs) {
                    stopwdSet.add(Converter.up2lowAndFull2half(c));
                }
            }
            return stopwdSet;
        }
    }
}
