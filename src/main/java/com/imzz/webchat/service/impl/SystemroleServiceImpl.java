package com.imzz.webchat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imzz.webchat.entity.Systemrole;
import com.imzz.webchat.mapper.SystemroleMapper;
import com.imzz.webchat.service.SystemroleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author : 邹智敏
 * @data : 2018年-04月-16日 16:36
 * @Description : 系统角色类
 **/
@Service("systemrole")
public class SystemroleServiceImpl extends ServiceImpl<SystemroleMapper, Systemrole>  implements SystemroleService {

    @Autowired
    private SystemroleMapper systemroleMapper;

    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
    @Override
    public List<Systemrole> roleList(String userNumber) {
        return systemroleMapper.roleList(userNumber);
    }
}
