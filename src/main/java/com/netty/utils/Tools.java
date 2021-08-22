package com.netty.utils;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author cxks
 * @Date 2021/8/22 23:29
 */
public class Tools {

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

    public static byte[] intToBytes(int num){
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (num >>> (24 - i * 8));
        }
        return b;
    }
}
