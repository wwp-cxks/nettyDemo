package com.netty.netty.framework;

import lombok.Data;

import io.netty.channel.socket.SocketChannel;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author cxks
 * @Date 2021/8/22 23:57
 */
@Data
public class NettyClient {

    private SocketChannel socketChannel;

    // TODO 持有一个当前客户端用户对象

    // TODO 持有当前客户端用户对象的聊天列表

    // TODO 持有当前客户端用户的好友情况
}
