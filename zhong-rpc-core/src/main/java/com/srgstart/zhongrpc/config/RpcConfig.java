package com.srgstart.zhongrpc.config;

import com.srgstart.zhongrpc.fault.retry.RetryStrategyKeys;
import com.srgstart.zhongrpc.fault.tolerant.TolerantStrategyKeys;
import com.srgstart.zhongrpc.loadbalancer.LoadBalancerKeys;
import com.srgstart.zhongrpc.serializer.SerializerKeys;
import lombok.Data;

/**
 * @author srgstart
 * @create 2025/04/22 14:18
 * @description Rpc配置类
 */
@Data
public class RpcConfig {

    /**
     * 名称
     */
    private String name = "zhong-rpc";

    /**
     * 版本号
     */
    private String version = "1.0";

    /**
     * 服务器主机名
     */
    private String serverHost = "localhost";

    /**
     * 服务器端口号
     */
    private Integer serverPort = 8080;

    /**
     * 是否启用mock
     */
    private boolean mock = false;

    /**
     * 序列化器
     */
    private String serializer = SerializerKeys.JDK;

    /**
     * 注册中心配置
     */
    private RegistryConfig registryConfig = new RegistryConfig();

    /**
     * 负载均衡器
     */
    private String loadBalancer = LoadBalancerKeys.ROUND_ROBIN;

    /**
     * 重试策略
     */
    private String retryStrategy = RetryStrategyKeys.NO;

    /**
     * 容错策略
     */
    private String tolerantStrategy = TolerantStrategyKeys.FAIL_FAST;

}
