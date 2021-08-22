package com.netty.netty.handler;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.netty.netty.dto.NettyRequest;
import com.netty.netty.framework.ControllerManager;
import com.netty.netty.framework.NettyClient;
import com.netty.netty.framework.NettyServer;
import com.netty.netty.vo.ControllerType;
import com.netty.utils.ConvertTools;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author cxks
 * @Date 2021/8/23 0:17
 */
public class MyNettyRequestHandler extends ChannelInboundHandlerAdapter {

    private ControllerManager controllerManager;

    public MyNettyRequestHandler(ControllerManager controllerManager, NettyServer nettyServer) {
        this.controllerManager = controllerManager;
        this.nettyServer = nettyServer;
    }

    private NettyServer nettyServer;

    /**
     * 类样式解码器
     */
    private Schema<NettyRequest> schema = RuntimeSchema.getSchema(NettyRequest.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().localAddress() + "已加入");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 移除NettyClient
        NettyClient needRemoveClient =
            nettyServer.getSocketChannelNettyClientMap().get(ctx.channel());
        nettyServer.getOnlineNettyClientList().remove(needRemoveClient);
        nettyServer.getSocketChannelNettyClientMap().remove(ctx.channel());
        System.out.println(ctx.channel().localAddress() + "已离开");
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        byte[] bytes = ConvertTools.byteBufToBytes(msg);
        if(bytes == null || bytes.length == 0){
            return;
        }
        // 进行转换
        NettyRequest nettyRequest = new NettyRequest();
        ProtobufIOUtil.mergeFrom(bytes,nettyRequest,schema);
        // nettyRequest请求分流与调用
        controllerManager.handlerRequest(nettyRequest,(SocketChannel) ctx.channel());
        super.channelRead(ctx, msg);
    }
}
