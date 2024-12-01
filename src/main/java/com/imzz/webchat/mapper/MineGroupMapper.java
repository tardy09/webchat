package com.imzz.webchat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imzz.webchat.entity.MineGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  我的分组 接口
 * </p>
 *
 * @author 邹智敏
 * @since 2018-11-24
 */
public interface MineGroupMapper extends BaseMapper<MineGroup> {

    /***
     * <p>@author:       james</p>
     * <p>@data:         18-11-24 - 下午1:13 </p>
     * <p>@param:        mineId  中文描述： </p>
     * <p>@return:       java.util.List<com.zzm.weixin.entity.MineGroup></p>
     * <p>@version:      v1.0</p>
     * <p>@description:  查询好友列表</p>
     ***/
    List<MineGroup> mineGroups(@Param("mineId") String mineId);
}
