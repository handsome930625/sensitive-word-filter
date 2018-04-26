package com.chaser.engine;

/**
 * description:引擎模式
 *
 * @author wangyj on 2018/4/26
 */
public enum ModeType {
    /**
     * 获取全部的敏感词
     */
    GET_WORDS,
    /**
     * 是否存在敏感词
     */
    HAS_WORD,
    /**
     * 敏感词替换
     */
    DO_FILTER;
}
