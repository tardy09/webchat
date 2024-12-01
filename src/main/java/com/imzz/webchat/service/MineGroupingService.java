package com.imzz.webchat.service;

import com.imzz.webchat.entity.MineGrouping;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 邹智敏
 * @since 2018-11-29
 */
public interface MineGroupingService extends IService<MineGrouping> {
    /*** \_____________________________/
     * <p>@author       |   James</p>
     * <p>@param        |   null  </p>
     * <p>@return       |   </P>
     * <p>@date         |   2018/11/29 10:36</P>
     * <p>@description  |   根据用户id获取群</p>
     ***/
    List<MineGrouping> getMineGrouping(String mineId);

    /*** \_____________________________/
     * <p>@author       |   James</p>
     * <p>@param        |   null  </p>
     * <p>@return       |   </P>
     * <p>@date         |   2018/11/29 13:47</P>
     * <p>@description  |   根据群id获取群成员id</p>
     ***/
    List<String> getMineId(String grounpingId);
}
