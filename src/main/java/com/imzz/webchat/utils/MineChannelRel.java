package com.imzz.webchat.utils;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>@Auther: zou zhi min</p>
 * <p>@Date: 18-11-24 16:53</p>
 * <p>@Description: 发送人和channel关联处理</p>
 */
public class MineChannelRel {

    private static ConcurrentHashMap<String, Channel> manage = new ConcurrentHashMap<>();

    public static void put(String senderId,Channel channel){
        manage.put(senderId,channel);
    }

    public static Channel getValue(String senderId){
        return manage.get(senderId);
    }

    public static boolean isExist(String senderId){
        if(manage.get(senderId) != null){
            //存在放回true
            return true;
        }else{
            return false;
        }
    }

    public static String getKey(Channel channel){
        for(String key: manage.keySet()){
            if(manage.get(key).equals(channel)){
                return key;
            }
        }
        return null;
    }

    public static void output(){
        for(HashMap.Entry<String,Channel> entry : manage.entrySet()){
            System.out.println("userid\t"+entry.getKey()+"\t对应的管道\t"+entry.getValue().id().asLongText());
        }
    }
}
