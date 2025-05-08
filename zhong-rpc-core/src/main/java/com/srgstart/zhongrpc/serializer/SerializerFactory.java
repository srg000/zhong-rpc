package com.srgstart.zhongrpc.serializer;

import com.srgstart.zhongrpc.spi.SpiLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * @author srgstart
 * @create 2025/04/23 14:44
 * @description 序列化器工厂（用于获取序列化器对象）
 */
public class SerializerFactory {

    static {
        SpiLoader.load(Serializer.class);
    }

    /**
     * 默认序列化器
     */
    private static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();

    /**
     * 获取序列化器实例
     * @param key 序列化器key
     * @return
     */
    public static Serializer getInstance(String key) {
        return SpiLoader.getInstance(Serializer.class, key);
    }
}
