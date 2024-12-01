package com.imzz.webchat;


import com.imzz.webchat.netty.WSServer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * <p>@Author: james</p>
 * <p>@Description: 当springboot启动成后，netty才开始启动</P>
 * <p>@Date: 创建日期 2018/11/21 18:48</P>
 * <p>@version V1.0</p>
 **/
@Component
public class NettyBooter implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        //判断上下文是否为空
        if(contextRefreshedEvent.getApplicationContext().getParent() == null){
            try {
                WSServer.getInstance().start(null);
            }catch (Exception e){
                System.err.println("e.getMessage() = " + e.getMessage());
            }
        }

    }
}
