package com.srgstart.zhongrpc.server;

import io.vertx.core.Vertx;

/**
 * @author srgstart
 * @create 2024/10/05 10:37
 * @description
 */
public class VertxHttpServer implements HttpServer {
    @Override
    public void doStart(int port) {
        // 创建 Vert.x 实例
        Vertx vertx = Vertx.vertx();

        // 创建 HTTP服务器
        io.vertx.core.http.HttpServer httpServer = vertx.createHttpServer();

        // 监听端口并处理请求
        httpServer.requestHandler(new HttpServerHandler());

        // 启动 HTTP服务器并监听指定端口
        httpServer.listen(port, result -> {
            if (result.succeeded()) {
                System.out.println("Server is now listening on port" + port);
            } else {
                System.out.println("Failed to start server: " + result.cause().getMessage());
            }
        });
    }
}
