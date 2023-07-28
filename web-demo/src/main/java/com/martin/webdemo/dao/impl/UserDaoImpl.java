package com.martin.webdemo.dao.impl;

import com.martin.webdemo.dao.UserDao;
import com.martin.webdemo.dto.UserRegisterRequest;
import com.martin.webdemo.model.User;
import com.martin.webdemo.rowmapper.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserDaoImpl implements UserDao {

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public User getUserById(Integer userId) {
        String sql = "SELECT user_id,email,password,created_date,last_modified_date "+
                     "FROM user WHERE user_id =:user_id";
        Map<String,Object> map = new HashMap<>();
        map.put("user_id",userId);

        List<User> results = namedParameterJdbcTemplate.query(sql,map, new UserRowMapper());

        if(results.size() > 0){
            return results.get(0);
        }else{
            return null;
        }
    }

    @Override
    public User getUserByEmail(String email) {
        String sql = "SELECT user_id,email,password,created_date,last_modified_date "+
                     "FROM user WHERE email =:email";
        Map<String,Object> map = new HashMap<>();
        map.put("email",email);

        List<User> results = namedParameterJdbcTemplate.query(sql,map, new UserRowMapper());

        if(results.size() > 0){
            return results.get(0);
        }else{
            return null;
        }
    }

    @Override
    public Integer createUser(UserRegisterRequest registerRequest) {
        String sql = "INSERT INTO user (email,password,created_date,last_modified_date) " +
                     "VALUES (:email,:password,:created_date,:last_modified_date)";
        Map<String,Object> map = new HashMap<>();
        map.put("email",registerRequest.getEmail());
        map.put("password",registerRequest.getPassword());

        Date now = new Date();
        map.put("created_date",now);
        map.put("last_modified_date",now);

        KeyHolder key = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql,new MapSqlParameterSource(map),key);

       Integer userId = key.getKey().intValue();

        return userId;
    }
}
