package com.imzz.webchat.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.imzz.webchat.entity.Mine;
import com.imzz.webchat.entity.Result;
import com.imzz.webchat.service.MineService;
import com.imzz.webchat.utils.MD5Utils;
import com.imzz.webchat.utils.ResultUtil;
import com.imzz.webchat.utils.VerifyCodeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.UUID;

/**
 * <p>@Auther: zou zhi min</p>
 * <p>@Date: 18-11-25 10:12</p>
 * <p>@Description: class description</p>
 */
@Controller
@RequestMapping("/login")
@Api(tags="用户登录管理",description="<font color='red'>【功能】登录、跳转到聊天页面</font>")
public class LoginController {

    @Autowired
    private MineService mineService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /*** \_____________________________/
     * <p>@author       |   James</p>
     * <p>@param        |   null  </p>
     * <p>@return       |   </P>
     * <p>@date         |   18-12-2 下午1:39</P>
     * <p>@description  |   跳转到登录页面</p>
     ***/
    @GetMapping("/index")
    public String getLoginPage(){
        return "login";
    }

    /*** \_____________________________/
     * <p>@author       |   James</p>
     * <p>@param        |     </p>
     * <p>@return       |   java.lang.String</P>
     * <p>@date         |   18-12-2 下午2:21</P>
     * <p>@description  |   跳转到聊天界面</p>
     ***/
    @GetMapping("/webcaht")
    public String getWebcaht(){
        return "web-chat";
    }

    /*** \_____________________________/
     * <p>@author       |   James</p>
     * <p>@param        |   mine  </p>
     * <p>@param        |   session  </p>
     * <p>@return       |   com.imzz.webchat.entity.Result</P>
     * <p>@date         |   2018/11/30 12:47</P>
     * <p>@description  |   根据账号和密码去登录</p>
     ***/
    @PostMapping("/getToLoginDate")
    @ResponseBody
    @ApiOperation(value = "简单的登录",notes = "简单的根据账号和密码去登录，开发所有功能就加入shiro")
    public Result getToLoginDate(@RequestBody Mine mine, HttpSession session){

        String error = "未知错误";

        try {
            //获取验证码
            String validateCode = (String) session.getAttribute("validateCode");

            //判断验证码是不是错误的
            if (StringUtils.isBlank(validateCode) || StringUtils.isBlank(mine.getVercode()) || !validateCode.equals(mine.getVercode())) {
                return ResultUtil.error(-1, "验证码为空或不正确");
            }else{
                mine.setVercode(null);
                session.removeAttribute("validateCode");
            }

            //判断账号或密码是否为空
            if (StringUtils.isBlank(mine.getUsernumber()) || StringUtils.isBlank(mine.getUserpassword())) {
                return ResultUtil.error(-1, "账号或密码为空");
            }

            //开始登录
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken toke = new UsernamePasswordToken(mine.getUsernumber(),mine.getUserpassword());
            subject.login(toke);
            if(subject.isAuthenticated()){
                //加密查询
                mine.setUserpassword(MD5Utils.getPwd(mine.getUserpassword(),mine.getUsernumber()));
                Mine result = mineService.getOne(new QueryWrapper<>(mine,"id","usernumber"));
                //把所需的信息存入redis中
                stringRedisTemplate.opsForValue().set(result.getUsernumber(),result.getId());
                //如果有账号，就登录
                return ResultUtil.success("webcaht");
            }
            }catch (IncorrectCredentialsException e) {
                error = "账号或密码错误!";
            } catch (ExcessiveAttemptsException e) {
                error = "登录失败次数过多!";
            } catch (LockedAccountException e) {
                error = "帐号已被锁定!";
            } catch (DisabledAccountException e) {
                error = "帐号已被禁用!";
            } catch (ExpiredCredentialsException e) {
                error = "帐号已过期!";
            } catch (UnknownAccountException e) {
                error = "帐号不存在!";
            } catch (UnauthorizedException e) {
                error = "您没有得到相应的授权！";
            }
        return ResultUtil.error(-1,error);
    }

    /*** \_____________________________/
     * <p>@author       |   James</p>
     * <p>@param        |   response  </p>
     * <p>@param        |   session  </p>
     * <p>@return       |   void</P>
     * <p>@date         |   2018/11/30 12:48</P>
     * <p>@description  |   登录页面的验证码</p>
     ***/
    @ApiOperation(value = "登录页面的验证码",notes = "登录页面的验证码")
    @GetMapping(value = "captcha")
    public void captcha(HttpServletResponse response, HttpSession session) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        //生成验证码
        String textCode = VerifyCodeUtil.generateTextCode(4,4,null);
        //白验证码放进缓存中，方便登录判断
        session.setMaxInactiveInterval(30*60);
        session.setAttribute("validateCode",textCode);
        //设置验证码的宽度
        int width = 200;
        //设置验证码的高度
        int height = 58;
        //设置验证码的干扰线
        int interLine = 2;
        //是否随机
        boolean randomLocation = true;
        //我这里设置验证码的背景颜色是白色
        Color backColor = new Color(255,255,255);
        //我这里设置字体颜色默认的颜色是黑色
        Color foreColor = new Color(66,2,82);
        //这是干扰线的颜色，null为随机颜色
        Color lineColor = null;
        BufferedImage image = VerifyCodeUtil.generateImageCode(textCode,width,height,interLine,randomLocation,backColor,foreColor,lineColor);
        // 输出图象到页面
        try {
            ImageIO.write(image, "jpeg", response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户退出
     */
    @RequestMapping("/logout")
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "login";
    }
}
