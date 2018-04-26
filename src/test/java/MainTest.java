import com.chaser.engine.SensitiveWordFilter;
import com.chaser.plugin.Fragment;
import com.chaser.plugin.Processor;

import java.util.HashSet;
import java.util.Set;

/**
 * description:
 *
 * @author wangyj on 2018/4/26
 */
public class MainTest {

    public static void main(String[] args) {
        // 1.word.txt 敏感词不完善
        SensitiveWordFilter sensitiveWordFilter = new SensitiveWordFilter();
        // 普通字符替换
        System.out.println(sensitiveWordFilter.replaceFilter("我帮你足交我帮你足交我帮你足交我帮你足交我帮你足交", "#"));
        // html 标签替换
        System.out.println(sensitiveWordFilter.highlightFilter("我帮你足交我帮你足交我帮你足交我帮你足交我帮你足交", "<span style='color:red'>", "</span>"));
        // 获取敏感词
        System.out.println(sensitiveWordFilter.getSensitiveWordList("性爱电影"));
        // 是否包含敏感词
        System.out.println(sensitiveWordFilter.hasSensitiveWord("我操尼玛"));
        // JDK 1.7 用法 使用自定义敏感词处理器
        System.out.println(sensitiveWordFilter.customDoFilter(new Fragment() {
            @Override
            public String format(String word) {
                return "sb" + word;
            }
        }, "我操死你个b崽子"));
        // JDK 1.8 使用自定义敏感词处理器
        System.out.println(sensitiveWordFilter.customDoFilter((word) -> "sb" + word, "我操死你个b崽子"));
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
        }, "我操死你个b崽子"));
        // 初始化引擎
        sensitiveWordFilter.reload();
    }
}
