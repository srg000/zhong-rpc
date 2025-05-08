package com.srgstart.zhongrpc.fault.retry;

import com.srgstart.zhongrpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * @author srgstart
 * @create 2025/05/07 14:01
 * @description 重试策略
 */
public interface RetryStrategy {

    /**
     * 重试接口
     * @param callable 可执行接口
     * @return RpcResponse
     * @throws Exception 异常
     */
    RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception;
}
