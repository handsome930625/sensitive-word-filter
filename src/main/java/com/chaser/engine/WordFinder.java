package com.chaser.engine;

import com.chaser.model.SensitiveWord;
import com.chaser.plugin.Processor;

import java.util.ArrayList;
import java.util.List;

/**
 * description:敏感词查找器
 *
 * @author wangyj on 2018/4/26
 */
public class WordFinder implements Processor {
    /**
     * 关键词
     */
    private List<SensitiveWord> sensitiveWordList = new ArrayList<>();

    @Override
    public void process(String text, int currentIndex) {
        SensitiveWord word = new SensitiveWord(text, currentIndex);
        int index = sensitiveWordList.indexOf(word);
        if (index > -1) {
            sensitiveWordList.get(index).addPosition(currentIndex, text);
        } else {
            sensitiveWordList.add(word);
        }
    }

    @Override
    public Object getReturnValue() {
        return sensitiveWordList;
    }
}
