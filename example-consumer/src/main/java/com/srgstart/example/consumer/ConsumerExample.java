package com.srgstart.example.consumer;

import com.srgstart.example.common.model.User;
import com.srgstart.example.common.service.UserService;
import com.srgstart.zhongrpc.bootstrap.ConsumerBootstrap;
import com.srgstart.zhongrpc.config.RpcConfig;
import com.srgstart.zhongrpc.constant.RpcConstant;
import com.srgstart.zhongrpc.proxy.ServiceProxyFactory;
import com.srgstart.zhongrpc.utils.ConfigUtils;

/**
 * @author srgstart
 * @create 2024/10/03 19:30
 * @description 简易版服务消费者示例
 */
public class ConsumerExample {
    public static void main(String[] args) {
        // 服务提供者初始化
        ConsumerBootstrap.init();

        // 获取代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("srgstart");
        // 调用
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser.getName());
        } else {
            System.out.println("user == null");
        }
    }
}
