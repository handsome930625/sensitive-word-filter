package com.chaser.model;

import java.util.HashMap;
import java.util.Map;

/**
 * description:trie 树节点
 *
 * @author wangyj on 2018/4/25
 */
public class Node {
    /**
     * 节点名称
     */
    private int value;
    /**
     * 根节点单词
     */
    private String word;
    /**
     * 每个节点可以走的分叉路
     */
    private Map<Integer, Node> table = new HashMap<>();
    /**
     * 该节点失败时回溯的节点
     */
    private Node fail;

    public Node(int value) {
        this.value = value;
    }

    public Boolean isWord() {
        return word != null;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Map<Integer, Node> getTable() {
        return table;
    }

    public void setTable(Map<Integer, Node> table) {
        this.table = table;
    }

    public Node getFail() {
        return fail;
    }

    public void setFail(Node fail) {
        this.fail = fail;
    }

    public int getValue() {
        return value;
    }
}
