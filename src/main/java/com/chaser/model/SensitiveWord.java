package com.chaser.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * description:敏感词抓取结果类
 *
 * @author wangyj on 2018/4/26
 */
public class SensitiveWord {
    /**
     * 关键词
     */
    private String word;
    /**
     * 敏感词匹配位置集合
     */
    private List<PositionNode> positions = new ArrayList<>();

    public SensitiveWord(String word, int end) {
        this.word = word;
        addPosition(end, word);
    }

    public void addPosition(int end, String word) {
        positions.add(new PositionNode(end - word.length() + 1, end));
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<PositionNode> getPositions() {
        return positions;
    }

    public void setPositions(List<PositionNode> positions) {
        this.positions = positions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SensitiveWord that = (SensitiveWord) o;
        return Objects.equals(word, that.word) &&
                Objects.equals(positions, that.positions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word, positions);
    }

    @Override
    public String toString() {
        return "SensitiveWord{" +
                "word='" + word + '\'' +
                ", positions=" + positions +
                '}';
    }
}
