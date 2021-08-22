package com.netty.netty.dto;

import lombok.Data;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author cxks
 * @Date 2021/8/23 0:16
 */
@Data
public class NettyRequest {
    public int controllerType;
    public int methodType;
    public String data;
}
