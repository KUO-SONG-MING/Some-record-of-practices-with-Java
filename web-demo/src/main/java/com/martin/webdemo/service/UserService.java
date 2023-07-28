package com.martin.webdemo.service;

import com.martin.webdemo.dto.UserLoginRequest;
import com.martin.webdemo.dto.UserRegisterRequest;
import com.martin.webdemo.model.User;

public interface UserService {
     User getUserById(Integer userId);
     Integer register(UserRegisterRequest userRegisterRequest);
     User login(UserLoginRequest userLoginRequest);
}
