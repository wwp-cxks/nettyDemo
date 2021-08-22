package com.netty;

import com.netty.netty.framework.NettyServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * CommandLineRunner 控制台运行器
 * 当springboot完全启动之后，所有的依赖已经完全注入，
 * 便会执行这个方法，可以避免我们手动调用接口
 * @author cxks
 */
@SpringBootApplication
public class NettyProjectApplication implements CommandLineRunner {

    private final NettyServer nettyServer;

    public NettyProjectApplication(NettyServer nettyServer) {
        this.nettyServer = nettyServer;
    }

    public static void main(String[] args) {
        SpringApplication.run(NettyProjectApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        nettyServer.startServer();
    }
}
