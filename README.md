# sensitive-word-filter

 &nbsp;&nbsp;&nbsp;&nbsp;该项目作为一个敏感词检测工具，具有敏感词获取、敏感词处理、敏感词替换等基本功能，还支持用户自己实现对应的敏感词获取器 和 敏感词解析器实现一些自定义的功能。


# 实现原理

   &nbsp;&nbsp;&nbsp;&nbsp;底层的实现原理采取的是AC自动机算法,AC自动机是在KMP算法和字典树上演变出来的一种多模匹配的算法。时间复杂度只取决于待分析文本的长度，和敏感词的数量无关。

 &nbsp;&nbsp;&nbsp;&nbsp;有兴趣的小伙伴可以阅读关于KMP 和 AC自动机相关的文档或者博客，相信你会收益颇丰

# 代码示例
    // 1.word.txt 敏感词不完善
    SensitiveWordFilter sensitiveWordFilter = new SensitiveWordFilter();
    // 普通字符替换
    System.out.println(sensitiveWordFilter.replaceFilter("待分析的文本", "#"));
    // html 标签替换
    System.out.println(sensitiveWordFilter.highlightFilter("待分析的文本", "<span style='color:red'>", "</span>"));
    // 获取敏感词
    System.out.println(sensitiveWordFilter.getSensitiveWordList("待分析的文本"));
    // 是否包含敏感词
    System.out.println(sensitiveWordFilter.hasSensitiveWord("待分析的文本"));
    // JDK 1.7 用法 使用自定义敏感词处理器
    System.out.println(sensitiveWordFilter.customDoFilter(new Fragment() {
        @Override
        public String format(String word) {
            return "sb" + word;
        }
    }, "我操死你个b崽子"));
    // JDK 1.8 使用自定义敏感词处理器
    System.out.println(sensitiveWordFilter.customDoFilter((word) -> "sb" + word, "待分析的文本"));
    // 自定义敏感词获取器
    System.out.println(sensitiveWordFilter.customGetWords(new Processor() {
        Set<String> sensitiveList = new HashSet<>();

        @Override
        public void process(String text, int currentIndex) {
            sensitiveList.add(text);
        }

        @Override
        public Object getReturnValue() {
            return sensitiveList;
        }
    }, "待分析的文本"));
    // 重载引擎
    sensitiveWordFilter.reload();
    
    
&nbsp;&nbsp;&nbsp;&nbsp;SensitiveWordFilter 类是方法的主要入口，负责初始化整个匹配引擎，目前默认实现是读取resources下word.txt（敏感词） 和 stop.txt（停顿词，表示引擎跳过该词）。<br>
&nbsp;&nbsp;&nbsp;&nbsp;你也可以通过实现 com.chaser.plugin.Resource 类，支持使用数据库或者接口之类的方式来获取资源注入到匹配引擎中。只需要调用SensitiveWordFilter对应的构造方法即可。

# 目录
![image](http://chuantu.biz/t6/296/1524735254x-1404758204.png)

1. engine ：匹配引擎的核心部分，默认实现了一些敏感词抓取器 和 敏感词处理器
2. model: 存放了一些引擎的节点的数据结构类
3. plugin：存放了一些接口，供使用者自己去实现，满足各种业务需求
4. utils：工具类

# 结束语
这个项目是工作之余阅读博客时开发，有一些错误或者实现方式上有些误区是难免的，希望大家发现错误时可以联系wangyj@homeking365.com。感谢！



