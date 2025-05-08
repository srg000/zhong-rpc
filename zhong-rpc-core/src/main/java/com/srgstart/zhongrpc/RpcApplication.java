package com.srgstart.zhongrpc;

import com.srgstart.zhongrpc.config.RegistryConfig;
import com.srgstart.zhongrpc.config.RpcConfig;
import com.srgstart.zhongrpc.constant.RpcConstant;
import com.srgstart.zhongrpc.registry.Registry;
import com.srgstart.zhongrpc.registry.RegistryFactory;
import com.srgstart.zhongrpc.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author srgstart
 * @create 2025/04/22 14:37
 * @description 相当于 holder，存放了项目全局用到的变量。双检锁单例模式实现
 */
@Slf4j
public class RpcApplication {

    private static volatile RpcConfig rpcConfig;

    /**
     * 初始化rpc配置，支持传入自定义配置
     * @param newRpcConfig 新的rpc配置
     */
    public static void init(RpcConfig newRpcConfig) {
        rpcConfig = newRpcConfig;
        log.info("rpc init, config:{}", rpcConfig);
        // 注册中心初始化
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        registry.init(registryConfig);
        log.info("registry init, config = {}", registryConfig);
        // 创建并注册 Shutdown Hook，JVM 退出时执行操作
        Runtime.getRuntime().addShutdownHook(new Thread(registry::destroy));
    }

    /**
     * 初始化rpc配置
     */
    public static void init() {
        RpcConfig newRpcConfig;
        try {
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        } catch (Exception e) {
            // 配置加载失败时，使用默认值
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }

    /**
     * 获取rpc配置
     * @return rpc配置
     */
    public static RpcConfig getRpcConfig() {
        if (rpcConfig == null) {
            synchronized (RpcApplication.class) {
                if (rpcConfig == null) {
                    init();
                }
            }
        }
        return rpcConfig;
    }
}
