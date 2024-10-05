package com.srgstart.example.common.model;

import java.io.Serializable;

/**
 * @author srgstart
 * @create 2024/10/03 17:04
 * @description 用户
 */
public class User implements Serializable {
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
