package com.netty.netty.framework;

import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.netty.netty.dto.NettyResponse;
import com.netty.netty.handler.MyNettyRequestHandler;
import com.netty.netty.vo.MethodType;
import com.netty.utils.ConvertTools;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author cxks
 * @Date 2021/8/22 23:54
 */
@Component
public class NettyServer {

    @Value("${netty.port}")
    private int port;

    private final ControllerManager controllerManager;
    private final NettyServer nettyServer;

    private ConcurrentHashMap<SocketChannel,NettyClient> socketChannelNettyClientMap;

    private List<NettyClient> onlineNettyClientList;

    public NettyServer(ControllerManager controllerManager, NettyServer nettyServer) {
        this.controllerManager = controllerManager;
        this.nettyServer = nettyServer;
    }

    /**
     * 发回响应信息给客户端
     * @param responseData
     * @param methodType
     * @param socketChannel
     */
    public static void sendResponseToClient(String responseData, MethodType methodType, SocketChannel socketChannel) {
        NettyResponse nettyResponse = new NettyResponse();
        nettyResponse.methodType = methodType.getValue();
        nettyResponse.data = responseData;
        // 转换为ByteBuf
        ByteBuf byteBuf = ConvertTools.nettyResponseToByteBuf(nettyResponse, RuntimeSchema.getSchema(NettyResponse.class));
        // 写入和冲刷
        socketChannel.writeAndFlush(byteBuf);

    }

    public ConcurrentHashMap<SocketChannel, NettyClient> getSocketChannelNettyClientMap() {
        return socketChannelNettyClientMap;
    }

    public List<NettyClient> getOnlineNettyClientList() {
        return onlineNettyClientList;
    }

    public void startServer(){
        socketChannelNettyClientMap = new ConcurrentHashMap<>();
        onlineNettyClientList = new ArrayList<>();

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
                        // 6.自定义业务客户端和自定义handler处理
                            System.out.println("报告：客户端连入：IP：" + socketChannel.localAddress().getHostName());
                            NettyClient client = new NettyClient();
                            client.setSocketChannel(socketChannel);
                            // 加入到在线客户端列表中
                            onlineNettyClientList.add(client);
                            // 加入到Map
                            socketChannelNettyClientMap.put(socketChannel, client);
                            // TODO 添加服务端自定义处理器handler
                            socketChannel.pipeline().addLast(new MyNettyRequestHandler(controllerManager, nettyServer));
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
