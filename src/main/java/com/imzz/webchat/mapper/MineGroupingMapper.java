package com.imzz.webchat.mapper;

import com.imzz.webchat.entity.MineGrouping;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  我的群接口
 * </p>
 *
 * @author 邹智敏
 * @since 2018-11-29
 */
public interface MineGroupingMapper extends BaseMapper<MineGrouping> {
    /*** \_____________________________/
     * <p>@author       |   James</p>
     * <p>@param        |   null  </p>
     * <p>@return       |   </P>
     * <p>@date         |   2018/11/29 10:36</P>
     * <p>@description  |   根据用户id获取群</p>
     ***/
    List<MineGrouping> getMineGrouping(@Param("mineId") String mineId);

    /*** \_____________________________/
     * <p>@author       |   James</p>
     * <p>@param        |   null  </p>
     * <p>@return       |   </P>
     * <p>@date         |   2018/11/29 13:47</P>
     * <p>@description  |   根据群id获取群成员id</p>
     ***/
    List<String> getMineId(@Param("grounpingId") String grounpingId);
}
