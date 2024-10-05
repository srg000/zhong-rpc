package com.srgstart.example.provider;

import com.srgstart.example.common.model.User;
import com.srgstart.example.common.service.UserService;

/**
 * @author srgstart
 * @create 2024/10/03 17:12
 * @description 用户服务实现类
 */
public class UserServiceImpl implements UserService {

    @Override
    public User getUser(User user) {
        System.out.println("用户名：" + user.getName());
        return user;
    }
}
