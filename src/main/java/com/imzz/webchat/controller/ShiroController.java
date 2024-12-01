package com.imzz.webchat.controller;

import com.imzz.webchat.entity.Result;
import com.imzz.webchat.service.impl.ShiroServiceImpl;
import com.imzz.webchat.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>@Author: james</p>
 * <p>@Description: 添加说明</P>
 * <p>@Date: 创建日期 2018/12/5 9:47</P>
 * <p>@version V1.0</p>
 **/
@RestController
@RequestMapping("/shiro")
public class ShiroController {

    @Autowired
    private ShiroServiceImpl shiroService;


    @GetMapping("/clear")
    public Result getClear(){

        if(shiroService.clearCache()){
            return ResultUtil.success("清空资源授权成功");
        }
        return ResultUtil.error(-1,"清空资源授权失败");
    }

}
