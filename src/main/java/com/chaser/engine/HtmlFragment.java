package com.chaser.engine;

import com.chaser.plugin.Fragment;
import org.apache.commons.lang3.StringUtils;

/**
 * description:html敏感词替换器
 *
 * @author wangyj on 2018/4/26
 */
public class HtmlFragment implements Fragment {
    /**
     * 开口标记
     */
    private String open;

    /**
     * 封闭标记
     */
    private String close;

    /**
     * 初始化开闭标签
     *
     * @param open  开始标签，如< b >、< font >等。
     * @param close 结束标签，如< /b >、< /font >等。
     */
    HtmlFragment(String open, String close) {
        this.open = StringUtils.trimToEmpty(open);
        this.close = StringUtils.trimToEmpty(close);
    }

    @Override
    public String format(String word) {
        return open + word + close;
    }
}
