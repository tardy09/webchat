package com.imzz.webchat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imzz.webchat.entity.Systemmenu;
import com.imzz.webchat.mapper.SystemmenuMapper;
import com.imzz.webchat.service.SystemmenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author : 邹智敏
 * @data : 2018年-04月-16日 16:39
 * @Description : 权限类
 **/
@Service("systemmenu")
public class SystemmenuServiceImpl extends ServiceImpl<SystemmenuMapper, Systemmenu> implements SystemmenuService {

    @Autowired
    private SystemmenuMapper systemmenuMapper;

    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
    @Override
    public List<Systemmenu> menuList(String systemroleId) {
        return systemmenuMapper.menuList(systemroleId);
    }
}
