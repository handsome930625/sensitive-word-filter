package com.chaser.model;

import java.util.Objects;

/**
 * description:敏感词位置类
 *
 * @author wangyj on 2018/4/26
 */
public class PositionNode {

    /**
     * 开始位置 从1开始
     */
    private int begin;
    /**
     * 结束位置
     */
    private int end;

    public PositionNode(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PositionNode that = (PositionNode) o;
        return begin == that.begin &&
                end == that.end;
    }

    @Override
    public int hashCode() {
        return Objects.hash(begin, end);
    }

    @Override
    public String toString() {
        return "PositionNode{" +
                "begin=" + begin +
                ", end=" + end +
                '}';
    }
}
