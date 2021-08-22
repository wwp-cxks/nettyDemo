package com.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author cxks
 * @Date 2021/8/22 22:39
 */
public class MyNettyServerHandler extends SimpleChannelInboundHandler<String> {

    private static int num = 0;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception{
        System.out.println("收到来自客户端的消息:" + s + "第：" + num + "次");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush("你好！我是netty服务器！");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端下线了");
    }
}
