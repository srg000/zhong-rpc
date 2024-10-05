package com.srgstart.example.common.service;
import com.srgstart.example.common.model.User;

/**
 * @author srgstart
 * @create 2024/10/03 17:07
 * @description 用户服务
 */
public interface UserService {

    /**
     * 获取用户
     * @param user
     * @return
     */
    User getUser(User user);
}
