package com.chaser.engine;

import com.chaser.plugin.Fragment;
import org.apache.commons.lang3.StringUtils;

/**
 * description:替换处理敏感词
 *
 * @author wangyj on 2018/4/26
 */
public class ReplaceFragment implements Fragment {

    /**
     * 替换标记
     */
    private String sign;

    ReplaceFragment(String sign) {
        this.sign = StringUtils.trimToEmpty(sign);
    }

    @Override
    public String format(String word) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            stringBuilder.append(sign);
        }
        return stringBuilder.toString();
    }
}
