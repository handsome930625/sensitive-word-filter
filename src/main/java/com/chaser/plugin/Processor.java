package com.chaser.plugin;

/**
 * description:处理文本的接口
 *
 * @author wangyj on 2018/4/26
 */
public interface Processor {
    /**
     * <p>功能描述: 敏感词解析器</p>
     * <p>创建人: wangyj </p>
     * <p>创建日期: 2018/4/26 10:58 </p>
     *
     * @param text         关键词
     * @param currentIndex 当前索引的位置
     */
    void process(String text, int currentIndex);

    /**
     * <p>功能描述: 获取敏感词的返回值 </p>
     * <p>创建人: wangyj </p>
     * <p>创建日期: 2018/4/26 12:24 </p>
     *
     * @return 获取敏感词的返回值
     */
    Object getReturnValue();
}
