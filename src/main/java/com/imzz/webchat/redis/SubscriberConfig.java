package com.imzz.webchat.redis;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * @ClassName: SubscriberConfig
 * @Description: 消息订阅者配置类：
 * @Author: Administrator
 * @Date: 2018/7/22   10:44
 * @Version: 1.0
 */

@Configuration
@AutoConfigureAfter({Receiver.class})
public class SubscriberConfig {
    /**
     * 消息监听适配器，注入接受消息方法，输入方法名字 反射方法
     * @param receiver
     * @return
     */
    @Bean
    public MessageListenerAdapter getMessageListenerAdapter(Receiver receiver) {
        //当没有继承MessageListener时需要写方法名字
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    /**
     * 创建消息监听容器
     * @param redisConnectionFactory
     * @param messageListenerAdapter
     * @return
     */
    @Bean
    public RedisMessageListenerContainer getRedisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory, MessageListenerAdapter messageListenerAdapter) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
        redisMessageListenerContainer.addMessageListener(messageListenerAdapter, new PatternTopic("TOPIC_USERNAME"));
        return redisMessageListenerContainer;
    }
}
