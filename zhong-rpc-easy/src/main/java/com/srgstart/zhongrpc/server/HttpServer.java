package com.srgstart.zhongrpc.server;

/**
 * @author srgstart
 * @create 2024/10/05 10:35
 * @description HTTP 服务器接口
 */
public interface HttpServer {

    /**
     * 启动HTTP服务器
     *
     * @param port
     */
    void doStart(int port);
}
