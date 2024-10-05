package com.srgstart.example.provider;

import com.srgstart.example.common.service.UserService;
import com.srgstart.zhongrpc.registry.LocalRegistry;
import com.srgstart.zhongrpc.server.HttpServer;
import com.srgstart.zhongrpc.server.VertxHttpServer;

/**
 * @author srgstart
 * @create 2024/10/03 17:14
 * @description 简易服务提供者示例
 */
public class EasyProviderExample {
    public static void main(String[] args) {

        // 注册服务
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);

        // 启动 web服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(8080);
    }
}
