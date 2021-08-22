package com.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author cxks
 * @Date 2021/8/22 22:29
 */
public class Main {

    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = null;
        NioEventLoopGroup workGroup = null;
        try {
        // 1.创建两个线程组，bossGroup,workGroup
        bossGroup = new NioEventLoopGroup(1);
        workGroup = new NioEventLoopGroup();

        // 2.创建服务启动助手
        ServerBootstrap bootstrap = new ServerBootstrap();
        // 3.将线程组设置给启动助手
        bootstrap.group(bossGroup, workGroup)
                // 4.指定通信channel的类型
                .channel(NioServerSocketChannel.class)
                // 5.指定channel的初始化器
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new StringEncoder());
                        socketChannel.pipeline().addLast(new StringDecoder());
                        // TODO 添加服务端自定义处理器handler
                        socketChannel.pipeline().addLast(new MyNettyServerHandler());
                    }
                });
            // 7.服务端启动助手绑定端口，并且设置同步运行方式，获得返回的channelFuture对象
           ChannelFuture channelFuture = bootstrap.bind(7788).sync();
            System.out.println("------服务端正在运行-----");
           // 8.配置channelFuture在未来以同步的方式关闭
           channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
