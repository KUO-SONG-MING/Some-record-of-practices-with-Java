package com.martin.webdemo.dao;

import com.martin.webdemo.dto.UserRegisterRequest;
import com.martin.webdemo.model.User;

public interface UserDao {

    public User getUserById(Integer userId);
    public  User getUserByEmail(String email);
    public Integer createUser(UserRegisterRequest registerRequest);
}
