package com.imzz.webchat.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.imzz.webchat.entity.Systemmenu;

import java.util.List;

/**
 * @author : 邹智敏
 * @data : 2018年-04月-16日 14:58
 * @Description : 这里是此类的说明
 **/
public interface SystemmenuService extends IService<Systemmenu> {

    /*** \_____________________________/
     * <p>@author       |   James</p>
     * <p>@param        |   systemroleId  </p>
     * <p>@return       |   java.util.List<com.imzz.webchat.entity.Systemmenu></P>
     * <p>@date         |   18-12-2 下午1:16</P>
     * <p>@description  |   根据角色id查询权限</p>
     ***/
    List<Systemmenu> menuList( String systemroleId);
}
