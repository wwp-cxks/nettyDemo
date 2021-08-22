package com.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author cxks
 * @Date 2021/8/22 22:46
 */
public class Main {

    public static void main(String[] args) {
        // 1.创建线程组
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        // 2.创建客户端启动助手
        Bootstrap bootstrap = new Bootstrap();
        // 3.设置客户端启动助手线程组
        bootstrap.group(eventLoopGroup)
                // 4.指定通信的channel类型
                .channel(NioSocketChannel.class)
                // 5.创建channel初始化器
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        // 6.完成channel初始化，指定编解码器以及自定义的handler
                        socketChannel.pipeline().addLast(new StringEncoder());
                        socketChannel.pipeline().addLast(new StringEncoder());
                        // TODO 添加客户端自定义处理器handler
                        socketChannel.pipeline().addLast(new MyNettyClientHandler());
                    }
                });
        try {
            // 7.连接服务器，已同步的方式，且获得返回的ChannelFuture对象
           ChannelFuture channelFuture =
                   bootstrap.connect("127.0.0.1",7788).sync();
           // 8.设置ChannelFuture对象在未来以同步的方式关闭
           channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
