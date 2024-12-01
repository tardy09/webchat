package com.imzz.webchat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imzz.webchat.entity.Systemmenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface SystemmenuMapper extends BaseMapper<Systemmenu> {

    /*** \_____________________________/
     * <p>@author       |   James</p>
     * <p>@param        |   systemroleId  </p>
     * <p>@return       |   java.util.List<com.imzz.webchat.entity.Systemmenu></P>
     * <p>@date         |   18-12-2 下午1:16</P>
     * <p>@description  |   根据角色id查询权限</p>
     ***/
    List<Systemmenu> menuList(@Param("systemroleId") String systemroleId);
}