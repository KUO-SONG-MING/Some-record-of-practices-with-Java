package com.martin.webdemo.rowmapper;

import com.martin.webdemo.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User();
        user.setUser_id(resultSet.getInt("user_id"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        user.setCreated_date(resultSet.getTimestamp("created_date"));
        user.setLast_modified_date(resultSet.getTimestamp("last_modified_date"));
        return user;
    }
}
