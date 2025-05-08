package com.srgstart.zhongrpc.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.srgstart.zhongrpc.model.RpcRequest;
import com.srgstart.zhongrpc.model.RpcResponse;
import com.srgstart.zhongrpc.serializer.JdkSerializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author srgstart
 * @create 2024/10/05 15:42
 * @description Mock 服务代理（JDK 动态代理）
 */
@Slf4j
public class MockServiceProxy implements InvocationHandler {
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
        Class<?> methodReturnType = method.getReturnType();
        log.info("mock invoke {}", method.getName());
        return getDefaultObject(methodReturnType);
    }

    /**
     * 生成指定类型的默认值对象
     * @param clazz 对象类型
     * @return 默认值对象
     */
    private Object getDefaultObject(Class<?> clazz) {
        // 判断指定类是否为 原始数据类型
        if (clazz.isPrimitive()) {
            if (clazz == int.class) {
                return 0;
            } else if (clazz == long.class) {
                return 0L;
            } else if (clazz == boolean.class) {
                return false;
            } else if (clazz == short.class) {
                return (short) 0;
            }
        }
        return null;
    }
}
