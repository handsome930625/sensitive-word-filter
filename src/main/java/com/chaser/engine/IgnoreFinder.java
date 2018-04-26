package com.chaser.engine;

import com.chaser.plugin.Processor;

/**
 * description:忽略敏感词抓取器
 *
 * @author wangyj on 2018/4/26
 */
public class IgnoreFinder implements Processor {
    @Override
    public void process(String text, int currentIndex) {

    }

    @Override
    public Object getReturnValue() {
        return null;
    }
}
