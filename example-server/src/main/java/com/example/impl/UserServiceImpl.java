package com.example.impl;

import com.example.api.UserService;
import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.sheledon.annotation.RpcService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Sheledon
 * @date 2021/11/20
 * @Version 1.0
 */
@RpcService
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User get(Integer id) {
        return userMapper.getById(id);
    }

    @Override
    public void save(User user) {
        userMapper.save(user);
    }

    @Override
    public void deleteById(int id) {
        userMapper.delete(id);
    }

    @Override
    public void updateById(User user) {
        userMapper.update(user);
    }
}
