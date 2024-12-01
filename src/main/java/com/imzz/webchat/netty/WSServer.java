package com.imzz.webchat.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.stereotype.Component;

/**
 * <p>@Author: james</p>
 * <p>@Description: netty启动类</P>
 * <p>@Date: 创建日期 2018/11/21 14:29</P>
 * <p>@version V1.0</p>
 **/
@Component
public class WSServer {

    private EventLoopGroup mainGroup;

    private EventLoopGroup subGroup;

    private ServerBootstrap serverBootstrap;

    private ChannelFuture channelFuture;

    private static class SingeWSServer{
        static final WSServer instance = new WSServer();
    }

    /*** \_____________________________/
     * <p>@author       |   James</p>
     * <p>@param        |   null </p>
     * <p>@return       |   com.zzm.weixin.netty.WSServer</P>
     * <p>@date         |   2018/11/21 18:47</P>
     * <p>@description  |   外部只能通过这个方法进行netty启动</p>
     ***/
    public static WSServer getInstance(){
        return SingeWSServer.instance;
    }

    public WSServer(){

        mainGroup = new NioEventLoopGroup();

        subGroup = new NioEventLoopGroup();

        serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(mainGroup,subGroup);

        serverBootstrap.channel(NioServerSocketChannel.class);

        serverBootstrap.childHandler(new WSServerInittialzer());
    }

    /*** \_____________________________/
     * <p>@author       |   James</p>
     * <p>@param        |   prot 端口号 </p>
     * <p>@return       |   void</P>
     * <p>@date         |   2018/11/21 18:46</P>
     * <p>@description  |   启动netty</p>
     ***/
    public void start(String prot){
        if(prot == null || prot.length() == 0){
            prot = "8088";
        }
        this.channelFuture = serverBootstrap.bind(Integer.valueOf(prot));
        System.err.println("  _   _      _   _           _  _         __       ____    __       ______ _             _ \n" +
                " | \\ | |    | | | |         | || |       /_ |     |___ \\  /_ |     |  ____(_)           | |\n" +
                " |  \\| | ___| |_| |_ _   _  | || |_       | |       __) |  | |     | |__   _ _ __   __ _| |\n" +
                " | . ` |/ _ \\ __| __| | | | |__   _|      | |      |__ <   | |     |  __| | | '_ \\ / _` | |\n" +
                " | |\\  |  __/ |_| |_| |_| |    | |    _   | |  _   ___) |  | |  _  | |    | | | | | (_| | |\n" +
                " |_| \\_|\\___|\\__|\\__|\\__, |    |_|   (_)  |_| (_) |____/   |_| (_) |_|    |_|_| |_|\\__,_|_|\n" +
                "                      __/ |                                                               ");
    }
}
