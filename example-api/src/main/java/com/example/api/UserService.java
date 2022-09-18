package com.example.api;

import com.example.entity.User;

/**
 * @author Sheledon
 * @date 2021/11/20
 * @Version 1.0
 */
public interface UserService {
    // 增删改查
    User get(Integer id);
    void save(User user);
    void deleteById(int id);
    void updateById(User user);
}
