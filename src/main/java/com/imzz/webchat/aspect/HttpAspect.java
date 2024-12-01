package com.imzz.webchat.aspect;

import com.alibaba.fastjson.JSON;
import com.imzz.webchat.utils.ForMatJSONStr;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : 邹智敏
 * @data : 2018/3/30
 * @Description :  切面答应日志
 **/
@Aspect
@Component
public class HttpAspect {

    private final static Logger logger = LoggerFactory.getLogger(HttpAspect.class);

    @Pointcut("execution(public * com.imzz.webchat.controller.*.*(..)))")
    public void valid() {}

    /**
     * 前置通知
     */
    @Before("valid()")
    public void doBefore(JoinPoint joinPoint) {

        logger.info("<=========================================开始请求====================================>");

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        HttpServletRequest request = attributes.getRequest();

        //url
        logger.info("[请\t\t求\t路\t径]{}", "["+request.getRequestURL()+"]");

        //method
        logger.info("[请\t\t求\t方\t法]{}", "["+request.getMethod()+"]");

        //ip
        logger.info("[远\t\t程\t地\t址]{}", "["+request.getRemoteAddr()+"]");

        //类方法
        logger.info("[请\t\t求\t方\t法]{}", "["+joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName()+"]");

        String str = joinPoint.getArgs().toString();
        //参数
        logger.info("[参\t\t数\t类\t型]{}", "["+str.substring(str.lastIndexOf(".")+1,str.lastIndexOf(";"))+"]");
    }

    /**
     * 后置通知
     */
    @After("valid()")
    public void doAfter() {
        logger.info("<=========================================结束请求====================================>");
    }

    /**
     * 通知后返回的数据
     * @param object
     */
    @AfterReturning(returning = "object", pointcut = "valid()")
    public void doAfterReturning(Object object) {
        try{
            logger.info("[返\t\t回\t参\t数]\n{}", ForMatJSONStr.format(JSON.toJSONString(object)));
        }catch (NullPointerException e){
            logger.info("[异\t\t常\t信\t息]{}","["+e.getMessage()+"]");
        }
    }
}
