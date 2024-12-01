package com.imzz.webchat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imzz.webchat.entity.Systemrole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SystemroleMapper extends BaseMapper<Systemrole> {
    /*** \_____________________________/
     * <p>@author       |   James</p>
     * <p>@param        |   userNumber  </p>
     * <p>@return       |   java.util.List<com.imzz.webchat.entity.Systemrole></P>
     * <p>@date         |   18-12-2 下午1:13</P>
     * <p>@description  |   根据用户账号查询角色</p>
     ***/
    List<Systemrole> roleList(@Param("userNumber") String userNumber);
}
