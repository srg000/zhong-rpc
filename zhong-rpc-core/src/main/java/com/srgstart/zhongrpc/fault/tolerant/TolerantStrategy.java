package com.srgstart.zhongrpc.fault.tolerant;

import com.srgstart.zhongrpc.model.RpcResponse;

import java.util.Map;

/**
 * @author srgstart
 * @create 2025/05/08 9:46
 * @description 容错策略
 */
public interface TolerantStrategy {

    /**
     * 容错方法
     * @param context 上下文，用于传递数据
     * @param e 异常
     * @return 容错处理后的结果
     */
    RpcResponse doTolerant(Map<String, Object> context, Exception e) throws Exception;
}
