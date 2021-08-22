package com.netty.utils;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.Schema;
import com.netty.netty.dto.NettyResponse;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author cxks
 * @Date 2021/8/23 0:32
 */
public class ConvertTools {

    /**
     * byte转为int数组
     * @param data
     * @return
     */
    public static int bytesToInt(byte[] data){
        int num = 0;
        for (int i = 0; i < 4; i++) {
            num <<= 8;
            num |= (data[i] & 0xff);
        }
        return num;
    }

    private static byte[] intToBytes(int num){
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (num >>> (24 - i * 8));
        }
        return b;
    }

    public static byte[] byteBufToBytes(Object msg){
        if(!(msg instanceof ByteBuf)){
            return null;
        }
        ByteBuf byteBuf = (ByteBuf) msg;
        // 先判断长度是否小于四
        if(byteBuf.readableBytes() < 4){
            return null;
        }

        byteBuf.markReaderIndex();
        byte[] lenBytes = new byte[4];
        // 获得消息体长度
        byteBuf.readBytes(lenBytes);
        int length = bytesToInt(lenBytes);
        // 判断消息体的长度是否接收够
        if(byteBuf.readableBytes() < length){
            byteBuf.resetReaderIndex();
            return null;
        }
        // 解析为消息体
        byte[] dataBytes = new byte[length];
        byteBuf.readBytes(dataBytes);
        return dataBytes;
    }

    public static ByteBuf nettyResponseToByteBuf(NettyResponse nettyResponse, Schema schema){
        LinkedBuffer buffer = LinkedBuffer.allocate(1024);
        byte[] msgData = ProtobufIOUtil.toByteArray(nettyResponse,schema,buffer);
        ByteBuf byteBuf = Unpooled.copiedBuffer(intToBytes(msgData.length),msgData);
        return byteBuf;
    }
}
