package com.srgstart.zhongrpc.loadbalancer;

import com.srgstart.zhongrpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;

/**
 * @author srgstart
 * @create 2025/05/07 10:09
 * @description 负载均衡器（消费端使用)
 */
public interface LoadBalancer {

    /**
     * 选择服务调用
     * @param requestParams 请求参数
     * @param serviceMetaInfoList 可用服务列表
     * @return 选择具体的服务
     */
    ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList);
}
