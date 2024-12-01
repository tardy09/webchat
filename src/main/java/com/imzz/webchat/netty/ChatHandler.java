package com.imzz.webchat.netty;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.imzz.webchat.entity.Chatmsg;
import com.imzz.webchat.entity.Mine;
import com.imzz.webchat.entity.MineGrouping;
import com.imzz.webchat.enums.MsgActionEnum;
import com.imzz.webchat.enums.MsgSignFlagEnum;
import com.imzz.webchat.service.ChatmsgService;
import com.imzz.webchat.service.MineGroupingService;
import com.imzz.webchat.service.MineService;
import com.imzz.webchat.service.impl.MineServiceImpl;
import com.imzz.webchat.utils.MineChannelRel;
import com.imzz.webchat.utils.SpringUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * <p>@Author: james</p>
 * <p>@Description: 处理消息的handler；TextWebSocketFrame在netty中yoghurt为websocket 专门处理文本的对象</P>
 * <p>@Date: 创建日期 2018/11/21 14:50</P>
 * <p>@version V1.0</p>
 **/
@Slf4j
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    /**
     * 建立管道组
     */
    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {

        //解析websocket传过来的json数据
        JSONObject jsonObject = (JSONObject)JSONObject.parse(msg.text());

        log.info("jsonobject+\t"+jsonObject);

        //获取推送人对象
        String pushManStr = jsonObject.get("mine").toString();
        //把发送人json转成发送人对象
        Mine pushMan = JSONObject.parseObject(pushManStr,Mine.class);
        log.info("当前发送人:\t"+pushMan);

        //判断管道组中有没有当前聊天的管道
        if(pushMan.getAction().equals(MsgActionEnum.CONNECT.type)){
            log.info("第一次进入或重新链接");

            StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) SpringUtil.getBean("stringRedisTemplate");
            pushMan.setId(stringRedisTemplate.opsForValue().get(pushMan.getUsernumber()));
            getInitConnect(pushMan,jsonObject,ctx.channel());

        }else if(pushMan.getAction().equals(MsgActionEnum.CHAT.type)){

            log.info("开始聊天");
            insertChatMsg(jsonObject,pushMan,ctx.channel());
        }else if(pushMan.getAction().equals(MsgActionEnum.SIGNED.type)){

            log.info("消息开始签收");
            msgSign(pushMan);
        }else{
            log.info("开始心跳检测");
        }
    }


    /***
     * <p>@author:       james</p>
     * <p>@data:         18-11-25 - 下午3:49 </p>
     * <p>@param:          中文描述： </p>
     * <p>@return:       void</p>
     * <p>@version:      v1.0</p>
     * <p>@description:  第一次连接或重连</p>
     ***/
    private void getInitConnect(Mine pushMan,JSONObject jsonObject,Channel channel){

        log.info("推送人的信息：\t"+pushMan);

        MineService mineService = (MineService)SpringUtil.getBean("mineServiceImpl");

        //修改用户状态
        Mine mine = new Mine();
        mine.setId(pushMan.getId());
        //将用户设置在线
        mine.setStatus("online");
        mineService.updateById(mine);

        //websocket第一次打开的时候，把推送人id和当前channel进行绑定
        MineChannelRel.put(pushMan.getId(),channel);
        //这里要把未签收的消息重新发送给当前登入的人
        ChatmsgService chatmsgService = (ChatmsgService) SpringUtil.getBean("chatmsgServiceImpl");
        //通过发送人的id查询未签收的消息
        List<Chatmsg> chatmsgList = chatmsgService.list(new QueryWrapper<>(new Chatmsg(null,null,pushMan.getId(),null,"0",null,null)));
        //判断未签收的消息存不存在
        if(chatmsgList.size() > 0){
            for(Chatmsg chatmsg : chatmsgList){
                //通过发送人的id查找发送人的详细信息
                mine = mineService.getOne(new QueryWrapper<>(new Mine(chatmsg.getSenderId()),"id","username","status","sign","avatar"));
                mine.setContent(chatmsg.getContext());
                mine.setFromid(mine.getId());
                mine.setTimestamp(chatmsg.getTimestamp());
                //通过发送人的id查询发送人的管道
                Channel pushManChannel = MineChannelRel.getValue(pushMan.getId());
                jsonObject.put("mine",mine);
                jsonObject.put("msgId",chatmsg.getId());
                jsonObject.put("type","friend");
                pushManChannel.writeAndFlush(new TextWebSocketFrame(jsonObject.toJSONString()));
            }
        }
    }


    /***
     * <p>@author:       james</p>
     * <p>@data:         18-11-25 - 下午1:42 </p>
     * <p>@param:        jsonObject  中文描述： </p>
     * <p>@return:       io.netty.channel.Channel</p>
     * <p>@version:      v1.0</p>
     * <p>@description:  消息未签收的业务逻辑</p>
     ***/
    private void insertChatMsg(JSONObject jsonObject,Mine pushMan,Channel channel){

        //获取被推送人对象
        String pushedPeopleStr = jsonObject.get("to").toString();
        //把接收人json转换成接收人对象
        Mine pushedPeople = JSONObject.parseObject(pushedPeopleStr,Mine.class);

        //消息id
        String msgId = UUID.randomUUID().toString().replaceAll("-","");

        //创建消息业务对象
        ChatmsgService chatmsgService = (ChatmsgService)SpringUtil.getBean("chatmsgServiceImpl");
        Chatmsg chatmsg = new Chatmsg();
        chatmsg.setId(msgId);
        chatmsg.setContext(pushMan.getContent());
        chatmsg.setReceiverId(pushedPeople.getId());
        chatmsg.setSenderId(pushMan.getId());
        chatmsg.setStatus(String.valueOf(MsgSignFlagEnum.unsign.type));
        chatmsg.setTimestamp(System.currentTimeMillis());
        chatmsg.setType(pushedPeople.getType());
        //保存消息到数据库中
        chatmsgService.save(chatmsg);

        //判断是不是群聊
        if(pushedPeople.getType().equals("group")){
            //通过该群，查询当前群中有哪些成员，根据成员查询成员管道，通过成员列表，群发所有有广告
            MineGroupingService mineGroupingService = (MineGroupingService)SpringUtil.getBean("mineGroupingServiceImpl");
            List<String> mineIds = mineGroupingService.getMineId(pushedPeople.getId());
            for(String mineId : mineIds){
                if(MineChannelRel.isExist(mineId)){
                    //判断当前窗口是不是当前人发送的
                    if(mineId.equals(pushMan.getId())){
                        //我就不发送了
                        continue;
                    }
                    Channel  receiverChannel = MineChannelRel.getValue(mineId);
                    //设置推送人与被推送人的关系
                    jsonObject.put("type",pushedPeople.getType());
                    //设置消息
                    jsonObject.put("msgId",msgId);
                    receiverChannel.writeAndFlush(new TextWebSocketFrame(jsonObject.toJSONString()));
                }
            }
        }else{
            //从全局用户channel管道中获取接收方的channel
            Channel  receiverChannel = MineChannelRel.getValue(pushedPeople.getId());
            if(null == receiverChannel){
                //使用第三方推送插件,比如短信推送
                log.info("开始第三方推送");
            }else{
                //从管道组中查询接收人的管道存不存在
                Channel findChannel = channelGroup.find(receiverChannel.id());
                //用户在线
                if(findChannel != null){
                    //设置推送人与被推送人的关系
                    jsonObject.put("type",pushedPeople.getType());
                    //设置消息
                    jsonObject.put("msgId",msgId);
                    receiverChannel.writeAndFlush(new TextWebSocketFrame(jsonObject.toJSONString()));
                }
            }
        }
    }

    /***
     * <p>@author:       james</p>
     * <p>@data:         18-11-25 - 下午3:47 </p>
     * <p>@param:        pushMan  中文描述： </p>
     * <p>@return:       void</p>
     * <p>@version:      v1.0</p>
     * <p>@description:  消息签收的业务逻辑</p>
     ***/
    private void msgSign(Mine pushMan){
        //消息签收
        ChatmsgService chatmsgService = (ChatmsgService)SpringUtil.getBean("chatmsgServiceImpl");
        String msgIdsStr = pushMan.getExtand();
        //消息id
        String msgIds[] = msgIdsStr.split(",");
        List<String> msgIdList = new ArrayList<>();
        for (String id : msgIds){
            if(StringUtils.isNotBlank(id)){
                msgIdList.add(id);
            }
        }
        if(msgIdList.size() > 0){
            chatmsgService.batchUPdateMsgSigned(msgIdList);
        }
    }

    /*** \_____________________________/
     * <p>@author       |   James</p>
     * <p>@param        |   ctx  </p>
     * <p>@return       |   void</P>
     * <p>@date         |   2018/11/21 14:58</P>
     * <p>@description  |   当客户端建立连接后，获取客户端的channle,放到channelGroup中去进行管理</p>
     ***/
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        channelGroup.add(ctx.channel());
    }

    /*** \_____________________________/
     * <p>@author       |   James</p>
     * <p>@param        |   ctx  </p>
     * <p>@return       |   void</P>
     * <p>@date         |   2018/11/21 14:59</P>
     * <p>@description  |   添加说明</p>
     ***/
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //当触发handlerRemoved，channelGroup会自动移除客户端的channle
        channelGroup.remove(ctx.channel());

        //根据mineId修改用户的煮状态
        MineService mineService = (MineService)SpringUtil.getBean("mineServiceImpl");

        Mine mine = new Mine();
        mine.setId(MineChannelRel.getKey(ctx.channel()));
        mine.setStatus("offline");

        mineService.updateById(mine);

    }

    /***
     * <p>@author:       james</p>
     * <p>@data:         18-11-24 - 下午4:13 </p>
     * <p>@param:        ctx  中文描述： </p>
     * <p>@param:        cause  中文描述： </p>
     * <p>@return:       void</p>
     * <p>@version:      v1.0</p>
     * <p>@description:  异常处理</p>
     ***/
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().closeFuture();
        channelGroup.remove(ctx.channel());
    }
}
