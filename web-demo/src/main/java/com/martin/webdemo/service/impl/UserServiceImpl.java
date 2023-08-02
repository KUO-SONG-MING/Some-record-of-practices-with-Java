package com.martin.webdemo.service.impl;

import com.martin.webdemo.dao.UserDao;
import com.martin.webdemo.dto.UserLoginRequest;
import com.martin.webdemo.dto.UserRegisterRequest;
import com.martin.webdemo.model.User;
import com.martin.webdemo.service.UserService;

import org.apache.tomcat.util.digester.Digester;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;

import org.slf4j.Logger;
@Component
public class UserServiceImpl implements UserService {
    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    UserDao userDao;

    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        User oldUser = userDao.getUserByEmail(userRegisterRequest.getEmail());

        //check whether email is used already
        if(oldUser != null){
            log.warn("Email {} had been registered",userRegisterRequest.getEmail());
            return -1;
            //throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //encode password with md5
        String hashedPassword = DigestUtils.md5DigestAsHex(userRegisterRequest.getPassword().getBytes());
        userRegisterRequest.setPassword(hashedPassword);

        Integer userId = userDao.createUser(userRegisterRequest);
        return userId;
    }

    @Override
    public User login(UserLoginRequest userLoginRequest) {
        User user = userDao.getUserByEmail(userLoginRequest.getEmail());

        //check email
        if(user == null)
        {
            log.warn("This account is not exist");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //encode password with md5
        String hashedPassword = DigestUtils.md5DigestAsHex(userLoginRequest.getPassword().getBytes());

        //check password
        if(user.getPassword().equals(hashedPassword)){
            return user;
        } else{
            log.warn("password was wrong");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public User lineLogin(String email, String lineUserId) {
        User user = userDao.getUserByEmail(email);

        if(user == null)
        {
            UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
            userRegisterRequest.setEmail(email);
            userRegisterRequest.setPassword(lineUserId);
            Integer userId = userDao.createUser(userRegisterRequest);
            user = userDao.getUserById(userId);
        }else if(!user.getPassword().equals(lineUserId))
        {
            log.warn("line access_token is not matched.");
           throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return user;
    }
}
