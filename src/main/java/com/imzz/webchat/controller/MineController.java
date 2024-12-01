package com.imzz.webchat.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imzz.webchat.entity.Mine;
import com.imzz.webchat.entity.MineGrouping;
import com.imzz.webchat.entity.Result;
import com.imzz.webchat.service.MineGroupService;
import com.imzz.webchat.service.MineGroupingService;
import com.imzz.webchat.service.MineService;
import com.imzz.webchat.utils.MD5Utils;
import com.imzz.webchat.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 邹智敏
 * @since 2018-11-24
 */
@Controller
@RequestMapping("/mine")
@Api(tags="个人信息管理",description="<font color='red'>【功能】初始化聊天面板、获取群列表、修改个性签名和在线状态同一接口</font>")
public class MineController {

    @Autowired
    private MineService mineService;

    @Autowired
    private MineGroupService mineGroupService;

    @Autowired
    private MineGroupingService mineGroupingService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /***
     * <p>@author:       james</p>
     * <p>@data:         18-11-25 - 上午9:08 </p>
     * <p>@param:        id  中文描述： </p>
     * <p>@return:       com.alibaba.fastjson.JSONObject</p>
     * <p>@version:      v1.0</p>
     * <p>@description:  初始化聊天列表</p>
     ***/
    @GetMapping("/getInit")
    @ApiOperation(value = "查询面板参数",notes = "初始化聊天列表")
    @ResponseBody
    public Result getInit(){
        try{
            Subject subject = SecurityUtils.getSubject();
            String userNumber = (String)subject.getPrincipal();
            String id = redisTemplate.opsForValue().get(userNumber);
            if(StringUtils.isBlank(id)){
                return ResultUtil.success(null);
            }
            JSONObject json = new JSONObject();
            Mine mine = new Mine();
            mine.setId(id);
            mine = mineService.getOne(new QueryWrapper<>(mine,"id","username","status","sign","avatar"));
            //获取我的个人信息
            json.put("mine",mine);
            //获取好友列表
            json.put("friend",(mineGroupService.mineGroups(mine.getId())));
            //获取我的群
            json.put("group",mineGroupingService.getMineGrouping(id));
            return ResultUtil.success(json);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResultUtil.success(null);
    }

    /***
     * <p>@author:       james</p>
     * <p>@data:         18-11-25 - 下午3:18 </p>
     * <p>@param:        id  中文描述： </p>
     * <p>@return:       com.zzm.weixin.entity.Result</p>
     * <p>@version:      v1.0</p>
     * <p>@description:  获取群成员</p>
     ***/
    @GetMapping("/getMineGrouping")
    @ApiOperation(value = "获取群成员",notes = "获取群成员")
    @ResponseBody
    public Result getMineGrouping(String id){
        JSONObject json = new JSONObject();
        json.put("list",mineService.mines(id));
        return ResultUtil.success(json);
    }


    /*** \_____________________________/
     * <p>@author       |   James</p>
     * <p>@param        |   mine  </p>
     * <p>@return       |   com.imzz.webchat.entity.Result</P>
     * <p>@date         |   2018/11/29 15:19</P>
     * <p>@description  |   修改个性签名和在线状态同一接口</p>
     ***/
    @PostMapping("/updateMine")
    @ApiOperation(value = "修改个性签名和在线状态同一接口",notes = "修改个性签名和在线状态同一接口")
    @ResponseBody
    public Result updateMineStatus(@RequestBody Mine mine){
        String id = redisTemplate.opsForValue().get(mine.getUsernumber());
        mine.setId(id);
        if(StringUtils.isNoneBlank(mine.getId())){
            boolean falg =  mineService.updateById(mine);
            if(falg){
                return ResultUtil.success("操作成功！！");
            }
            return ResultUtil.success("功能失效，请联系管理员");
        }else{
            return ResultUtil.error(-1,"参数传入有误，请重新传入");
        }
    }

    /*** \_____________________________/
     * <p>@author       |   James</p>
     * <p>@param        |   mine  </p>
     * <p>@return       |   com.imzz.webchat.entity.Result</P>
     * <p>@date         |   2018/11/30 12:56</P>
     * <p>@description  |   用户注册</p>
     ***/
    @PostMapping("/insertMine")
    @ApiOperation(value = "账号注册",notes = "聊天账号注册")
    @ResponseBody
    public Result insertMine(@RequestBody Mine mine) throws Exception {

        QueryWrapper<Mine> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Mine::getUsernumber, mine.getUsernumber());
        //判断账号有没有注册
        if(mineService.count(queryWrapper) > 0){
            return ResultUtil.error(-1,"【"+mine.getUsernumber()+"】已存在");
        }

        mine.setSign("皮~~");
        mine.setAvatar("/images/kf.jpg");
        mine.setId(UUID.randomUUID().toString().replaceAll("-",""));
        mine.setStatus("offline");
        mine.setUserpassword(MD5Utils.getMD5Str(mine.getUserpassword()));
        if(mineService.save(mine)){
            return ResultUtil.success("注册成功，即将跳转到登录页面...");
        }else{
            return ResultUtil.error(-1,"注册失败");
        }
    }
}

