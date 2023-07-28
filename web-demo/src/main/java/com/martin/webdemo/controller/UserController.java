package com.martin.webdemo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.martin.webdemo.dto.UserLoginRequest;
import com.martin.webdemo.dto.UserRegisterRequest;
import com.martin.webdemo.model.User;
import com.martin.webdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    //我自己改成傳出json字串
    @PostMapping("/users/register")
    public ResponseEntity<String> register(@RequestBody @Valid UserRegisterRequest userRegisterRequest)
    {
       Integer userId = userService.register(userRegisterRequest);

       if(userId == -1)
       {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This email had been registered");
       }

       try
       {
           User user = userService.getUserById(userId);
           ObjectMapper objectMapper = new ObjectMapper();
           String jsonString = objectMapper.writeValueAsString(user);
           return ResponseEntity.status(HttpStatus.CREATED).body(jsonString);
       }
       catch (Exception e)
       {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unknown error");
       }
    }

    @PostMapping("/users/login")
    public ResponseEntity<User> login(@RequestBody @Valid UserLoginRequest userLoginRequest)
    {
        User user = userService.login(userLoginRequest);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
