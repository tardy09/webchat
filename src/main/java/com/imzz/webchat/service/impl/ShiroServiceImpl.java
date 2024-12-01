package com.imzz.webchat.service.impl;

import com.imzz.webchat.shiro.ShiroRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.springframework.stereotype.Service;

/**
 * <p>@Author: james</p>
 * <p>@Description: 添加说明</P>
 * <p>@Date: 创建日期 2018/12/5 9:40</P>
 * <p>@version V1.0</p>
 **/
@Service
public class ShiroServiceImpl {


    /*** \_____________________________/
     * <p>@author       |   James</p>
     * <p>@param        |        </p>
     * <p>@return       |   boolean</P>
     * <p>@date         |   2018/12/5 9:42</P>
     * <p>@description  |   清楚shiro权限</p>
     ***/
    public boolean clearCache(){
        RealmSecurityManager rsm = (RealmSecurityManager)SecurityUtils.getSecurityManager();
        ShiroRealm authRealm = (ShiroRealm)rsm.getRealms().iterator().next();
        authRealm.clearAuthz();
        return true;
    }

}
