package com.srgstart.zhongrpc.serializer;

import java.io.IOException;

/**
 * @author srgstart
 * @create 2024/10/05 11:16
 * @description 序列化器接口
 */
public interface Serializer {

    /**
     * 序列化
     *
     * @param object
     * @return
     * @param <T>
     * @throws IOException
     */
    <T> byte[] serialize(T object) throws IOException;


    /**
     * 反序列化
     * @param bytes
     * @param type
     * @return
     * @param <T>
     * @throws IOException
     */
    <T> T deserialize(byte[] bytes, Class<T> type) throws IOException;
}
