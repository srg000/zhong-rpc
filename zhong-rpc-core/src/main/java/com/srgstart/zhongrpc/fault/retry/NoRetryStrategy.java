package com.srgstart.zhongrpc.fault.retry;

import com.srgstart.zhongrpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * @author srgstart
 * @create 2025/05/07 14:11
 * @description 不重试 - 重试策略
 */
public class NoRetryStrategy implements RetryStrategy{
    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        return callable.call();
    }
}
