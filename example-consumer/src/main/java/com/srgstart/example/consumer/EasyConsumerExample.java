package com.srgstart.example.consumer;

import com.srgstart.example.common.model.User;
import com.srgstart.example.common.service.UserService;
import com.srgstart.zhongrpc.proxy.ServiceProxyFactory;

/**
 * @author srgstart
 * @create 2024/10/03 19:30
 * @description 简易版服务消费者示例
 */
public class EasyConsumerExample {
    public static void main(String[] args) {
//        // 静态代理
//        UserService userService = new UserServiceProxy();
        // 动态代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("zhonghongxun");
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println("获取用户成功：" + newUser.getName());
        } else {
            System.out.println("获取用户失败");
        }
    }
}
