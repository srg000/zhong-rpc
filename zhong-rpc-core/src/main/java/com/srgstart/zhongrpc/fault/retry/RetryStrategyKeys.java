package com.srgstart.zhongrpc.fault.retry;

/**
 * @author srgstart
 * @create 2025/05/07 14:11
 * @description 重试策略键名常量
 */
public interface RetryStrategyKeys {

    /**
     * 不重试
     */
    String NO = "no";

    /**
     * 固定时间间隔
     */
    String FIXED_INTERVAL = "fixedInterval";

}
