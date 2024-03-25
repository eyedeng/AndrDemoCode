package com.example.andrdemocode.ipc.model;

import java.io.Serializable;

/**
 * @author dengyan
 * @date 2024/3/20
 * @desc 序列化方式1：Java的，简单，开销大，可用在储存设备、网络传输
 */
public class User implements Serializable {
    private static final long serialVersionUID = 20240320L;
    public int userId;
    public String userName;
    public boolean isStudent;

    public User(int userId, String userName, boolean isStudent) {
        this.userId = userId;
        this.userName = userName;
        this.isStudent = isStudent;
    }

    @Override
    public String toString() {
        return "User{" +
            "userId=" + userId +
            ", userName='" + userName + '\'' +
            ", isStudent=" + isStudent +
            '}';
    }
}
