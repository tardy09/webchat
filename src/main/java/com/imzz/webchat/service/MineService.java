package com.imzz.webchat.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.imzz.webchat.entity.Mine;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 邹智敏
 * @since 2018-11-24
 */
public interface MineService extends IService<Mine> {

    /***
     * <p>@author:       james</p>
     * <p>@data:         18-11-25 - 下午2:22 </p>
     * <p>@param:        groupingId  中文描述： </p>
     * <p>@return:       java.util.List<com.zzm.weixin.entity.Mine></p>
     * <p>@version:      v1.0</p>
     * <p>@description:  根据群组id获取群成员</p>
     ***/
    List<Mine> mines(String groupingId);

    /*** \_____________________________/
     * <p>@author       |   James</p>
     * <p>@param        |   pageIndex  </p>
     * <p>@param        |   pageSize  </p>
     * <p>@param        |   id  </p>
     * <p>@return       |   java.util.List<com.imzz.webchat.entity.Mine></P>
     * <p>@date         |   2018/11/30 14:27</P>
     * <p>@description  |   根据id分页查询</p>
     ***/
    List<Mine> mineList(Integer pageIndex,Integer pageSize,String id);

}
