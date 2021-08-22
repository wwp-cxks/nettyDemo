package com.netty.web.controller;

import com.netty.netty.framework.NettyServer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author cxks
 * @Date 2021/8/22 23:51
 */
@Controller
@RequestMapping("/system")
public class SystemController {

    private final NettyServer nettyServer;

    public SystemController(NettyServer nettyServer) {
        this.nettyServer = nettyServer;
    }

    @GetMapping("/test/{arg}")
    @ResponseBody
    public String test(@PathVariable String arg){
        return "输入的字符串为：" + arg;
    }

    @GetMapping("/startServer")
    public void startServer(){
        nettyServer.startServer();
    }
}
