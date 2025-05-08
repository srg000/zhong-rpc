package com.srgstart.zhongrpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.srgstart.zhongrpc.RpcApplication;
import com.srgstart.zhongrpc.config.RegistryConfig;
import com.srgstart.zhongrpc.config.RpcConfig;
import com.srgstart.zhongrpc.constant.RpcConstant;
import com.srgstart.zhongrpc.fault.retry.RetryStrategy;
import com.srgstart.zhongrpc.fault.retry.RetryStrategyFactory;
import com.srgstart.zhongrpc.fault.tolerant.TolerantStrategy;
import com.srgstart.zhongrpc.fault.tolerant.TolerantStrategyFactory;
import com.srgstart.zhongrpc.loadbalancer.LoadBalancer;
import com.srgstart.zhongrpc.loadbalancer.LoadBalancerFactory;
import com.srgstart.zhongrpc.model.RpcRequest;
import com.srgstart.zhongrpc.model.RpcResponse;
import com.srgstart.zhongrpc.model.ServiceMetaInfo;
import com.srgstart.zhongrpc.protocol.*;
import com.srgstart.zhongrpc.registry.Registry;
import com.srgstart.zhongrpc.registry.RegistryFactory;
import com.srgstart.zhongrpc.serializer.JdkSerializer;
import com.srgstart.zhongrpc.serializer.Serializer;
import com.srgstart.zhongrpc.serializer.SerializerFactory;
import com.srgstart.zhongrpc.server.tcp.VertxTcpClient;
import io.netty.util.concurrent.CompleteFuture;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * @author srgstart
 * @create 2024/10/05 15:42
 * @description 服务代理（JDK 动态代理）
 */
@Slf4j
public class ServiceProxy implements InvocationHandler {
    /**
     * 调用代理（当调用某个接口的方法时，会改为调用invoke方法）
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 指定序列化器
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());
        log.info("use Serialize info: {}", RpcApplication.getRpcConfig().getSerializer());

        // 构造请求
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();

        try {
            // 序列化
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            // 使用注册中心和服务发现机制解决
            RegistryConfig registryConfig = RpcApplication.getRpcConfig().getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfos = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfos)) {
                throw new RuntimeException("No service found for " + serviceMetaInfo.getServiceKey());
            }

            // 负载均衡
            LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(RpcApplication.getRpcConfig().getLoadBalancer());
            // 将调用方法名（请求路径）作为负载均衡参数
            Map<String, Object> requestParam = new HashMap<>();
            requestParam.put("methodName", rpcRequest.getMethodName());
            ServiceMetaInfo selectServiceMetaInfo = loadBalancer.select(requestParam, serviceMetaInfos);

            // 发送请求（Http）
//            try (HttpResponse httpResponse = HttpRequest.post(selectServiceMetaInfo.getServiceAddress())
//                    .body(bodyBytes)
//                    .execute()) {
//                byte[] result = httpResponse.bodyBytes();
//                // 反序列化
//                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
//                return rpcResponse.getData();
//            }
            // 使用重试机制 发送 Tcp 请求
            RpcResponse rpcResponse;
            try {
                RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(RpcApplication.getRpcConfig().getRetryStrategy());
                rpcResponse = retryStrategy.doRetry(() ->
                    VertxTcpClient.doRequest(rpcRequest, selectServiceMetaInfo)
                );
            } catch (Exception e) {
                // 容错机制
                TolerantStrategy tolerantStrategy = TolerantStrategyFactory.getInstance(RpcApplication.getRpcConfig().getTolerantStrategy());
                Map<String, Object> context = new HashMap<>();
                context.put("rpcRequest", rpcRequest);
                context.put("serviceMetaInfos", serviceMetaInfos);
                context.put("selectServiceMetaInfo", selectServiceMetaInfo);
                rpcResponse = tolerantStrategy.doTolerant(context, e);
            }
            return rpcResponse.getData();
        } catch (IOException e) {
            throw new RuntimeException("调用失败");
        }
    }
}
