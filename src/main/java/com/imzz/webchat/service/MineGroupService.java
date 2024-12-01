package com.imzz.webchat.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.imzz.webchat.entity.MineGroup;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 邹智敏
 * @since 2018-11-24
 */
public interface MineGroupService extends IService<MineGroup> {

    /***
     * <p>@author:       james</p>
     * <p>@data:         18-11-24 - 下午1:13 </p>
     * <p>@param:        mineId  中文描述： </p>
     * <p>@return:       java.util.List<com.zzm.weixin.entity.MineGroup></p>
     * <p>@version:      v1.0</p>
     * <p>@description:  查询好友列表</p>
     ***/
    List<MineGroup> mineGroups(String mineId);
}
