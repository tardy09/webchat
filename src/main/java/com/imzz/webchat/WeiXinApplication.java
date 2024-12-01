package com.imzz.webchat;


import com.imzz.webchat.utils.SpringUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.WebApplicationInitializer;

/**
 * <p>@Author: james</p>
 * <p>@Description: 添加说明</P>
 * <p>@Date: 创建日期 2018/11/21 18:19</P>
 * <p>@version V1.0</p>
 **/
@EnableTransactionManagement
@SpringBootApplication
@MapperScan("com.imzz.webchat.mapper")
public class WeiXinApplication  extends SpringBootServletInitializer implements WebApplicationInitializer  {

    @Bean
    public SpringUtil getSpringUtil(){
        return new SpringUtil();
    }

    public static void main(String[] args) {
        SpringApplication.run(WeiXinApplication.class,args);
    }
}
