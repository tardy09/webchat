package com.imzz.webchat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.imzz.webchat.entity.Systemrole;

import java.util.List;

/**
 * @author : 邹智敏
 * @data : 2018年-04月-16日 14:58
 * @Description : 这里是此类的说明
 **/
public interface SystemroleService extends IService<Systemrole> {
    /*** \_____________________________/
     * <p>@author       |   James</p>
     * <p>@param        |   userNumber  </p>
     * <p>@return       |   java.util.List<com.imzz.webchat.entity.Systemrole></P>
     * <p>@date         |   18-12-2 下午1:13</P>
     * <p>@description  |   根据用户账号查询角色</p>
     ***/
    List<Systemrole> roleList(String userNumber);
}
