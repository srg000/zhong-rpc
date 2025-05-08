package com.srgstart.zhongrpc.fault.tolerant;

import cn.hutool.core.collection.CollUtil;
import com.srgstart.zhongrpc.RpcApplication;
import com.srgstart.zhongrpc.config.RpcConfig;
import com.srgstart.zhongrpc.fault.retry.RetryStrategy;
import com.srgstart.zhongrpc.fault.retry.RetryStrategyFactory;
import com.srgstart.zhongrpc.loadbalancer.LoadBalancer;
import com.srgstart.zhongrpc.loadbalancer.LoadBalancerFactory;
import com.srgstart.zhongrpc.model.RpcRequest;
import com.srgstart.zhongrpc.model.RpcResponse;
import com.srgstart.zhongrpc.model.ServiceMetaInfo;
import com.srgstart.zhongrpc.server.tcp.VertxTcpClient;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author srgstart
 * @create 2025/05/08 9:46
 * @description 转移到其他服务节点 - 容错策略
 */
@Slf4j
public class FailOverTolerantStrategy implements TolerantStrategy {

    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        //获取其它节点并调用
        RpcRequest rpcRequest = (RpcRequest) context.get("rpcRequest");
        List<ServiceMetaInfo> serviceMetaInfoList = (List<ServiceMetaInfo>) context.get("serviceMetaInfos");
        ServiceMetaInfo selectedServiceMetaInfo = (ServiceMetaInfo) context.get("selectServiceMetaInfo");

        //移除失败节点
        removeFailNode(selectedServiceMetaInfo,serviceMetaInfoList);

        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
        Map<String, Object> requestParamMap = new HashMap<>();
        requestParamMap.put("methodName", rpcRequest.getMethodName());

        RpcResponse rpcResponse = null;
        while (serviceMetaInfoList.size() > 0) {
            ServiceMetaInfo currentServiceMetaInfo = loadBalancer.select(requestParamMap, serviceMetaInfoList);
            System.out.println("获取节点：" + currentServiceMetaInfo);
            try {
                //发送tcp请求
                RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetryStrategy());
                rpcResponse = retryStrategy.doRetry(() -> VertxTcpClient.doRequest(rpcRequest, currentServiceMetaInfo));
                return rpcResponse;
            } catch (Exception exception) {
                //移除失败节点
                removeFailNode(currentServiceMetaInfo,serviceMetaInfoList);
            }
        }
        //调用失败
        throw new RuntimeException(e);
    }

    /**
     * 移除失败节点，可考虑下线
     *
     * @param serviceMetaInfoList 服务节点列表
     */
    private void removeFailNode(ServiceMetaInfo currentServiceMetaInfo, List<ServiceMetaInfo> serviceMetaInfoList) {
        if (CollUtil.isNotEmpty(serviceMetaInfoList)) {
            serviceMetaInfoList.removeIf(next -> currentServiceMetaInfo.getServiceNodeKey().equals(next.getServiceNodeKey()));
        }
    }
}
