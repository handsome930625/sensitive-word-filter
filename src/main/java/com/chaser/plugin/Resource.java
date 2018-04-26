package com.chaser.plugin;

import java.util.List;

/**
 * description: 资源文件获取接口 开发给实现者自己去实现
 *
 * @author wangyj on 2018/4/25
 */
public interface Resource {

    /**
     * <p>功能描述: 读取资源转换成关键字，实现者可以实现该接口</p>
     * <p>创建人: wangyj </p>
     * <p>创建日期: 2018/4/25 16:57 </p>
     *
     * @return 关键词list
     */
    List<String> readWords();
}
