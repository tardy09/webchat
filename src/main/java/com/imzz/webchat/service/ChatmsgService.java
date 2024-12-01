package com.imzz.webchat.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.imzz.webchat.entity.Chatmsg;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 邹智敏
 * @since 2018-11-24
 */
public interface ChatmsgService extends IService<Chatmsg> {
    /***
     * <p>@author:       james</p>
     * <p>@data:         18-11-24 - 下午6:30 </p>
     * <p>@param:        list  中文描述： </p>
     * <p>@return:       int</p>
     * <p>@version:      v1.0</p>
     * <p>@description:  根据id,批量修改消息签收状态</p>
     ***/
    int batchUPdateMsgSigned(List<String> list);
}
