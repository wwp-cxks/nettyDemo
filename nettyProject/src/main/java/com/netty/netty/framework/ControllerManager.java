package com.netty.netty.framework;

import cn.hutool.core.util.StrUtil;
import com.netty.netty.dto.NettyRequest;
import com.netty.netty.vo.ControllerType;
import com.netty.netty.vo.MethodType;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.internal.ObjectUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author cxks
 * @Date 2021/8/23 0:56
 */
@Component
public class ControllerManager {

    private HashMap<ControllerType, BaseController> controllerHashMap;

    /**
     * 执行请求
     * @param nettyRequest
     * @param socketChannel
     */
    public void handlerRequest(NettyRequest nettyRequest, SocketChannel socketChannel){
        // 根据NettyRequest的所属Controller，拿到对应的Controller的实例
       ControllerType controllerType =
               ControllerType.values()[nettyRequest.controllerType];
       BaseController controllerInstance = controllerHashMap.get(controllerType);
       if(controllerInstance == null){
           System.out.println("没有找到controllerType:" + controllerType + "的controller实例");
       }
       // 获取方法
        MethodType methodType = MethodType.getMethodType(nettyRequest.methodType);
        try {
            Method method = controllerInstance.getClass().getMethod(methodType.getMethodName());
            if(method == null){
                System.out.println("controller：" + controllerType + "未找到：" + methodType.getMethodName() + "方法");
                return;
            }
            String responseData =
                    (String) method.invoke(controllerInstance,nettyRequest.data,socketChannel);
            if(StrUtil.isNotEmpty(responseData)){
                // TODO 调用nettyServer中，向客户端发回响应的方法
                NettyServer.sendResponseToClient(responseData,methodType,socketChannel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
