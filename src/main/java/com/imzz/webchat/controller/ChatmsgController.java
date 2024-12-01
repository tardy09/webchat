package com.imzz.webchat.controller;


import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 邹智敏
 * @since 2018-11-24
 */
@RestController
@RequestMapping("/chatmsg")
@Api(tags="用户特征管理",description="<font color='red'>【功能】分页查询、单个查询、按条件查询、查询所有、查询总记录数、单个新增、单个修改、单个删除、批量删除、按条件删除</font>")
public class ChatmsgController {

}

