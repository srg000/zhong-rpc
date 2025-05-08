package com.srgstart.zhongrpc.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;

/**
 * @author srgstart
 * @create 2025/04/22 14:19
 * @description 配置工具类
 */
public class ConfigUtils {

    /**
     * 加载配置文件
     * @param tClass 配置类
     * @param prefix 配置前缀
     * @return 配置对象
     * @param <T> 配置对象类型
     */
    public static <T> T loadConfig(Class<T> tClass, String prefix) {
        return loadConfig(tClass, prefix, "");
    }

    /**
     * 加载配置文件，支持区分环境
     * @param tClass 配置类
     * @param prefix 配置前缀
     * @param environment 环境
     * @return 配置对象
     * @param <T> 配置对象类型
     */
    public static <T> T loadConfig(Class<T> tClass, String prefix, String environment) {
        StringBuilder configFileBuilder = new StringBuilder("application");
        if (StrUtil.isNotEmpty(environment)) {
            configFileBuilder.append("-").append(environment);
        }
        configFileBuilder.append(".properties");
        Props props = new Props(configFileBuilder.toString());
        props.autoLoad(true);
        return props.toBean(tClass, prefix);
    }
}
