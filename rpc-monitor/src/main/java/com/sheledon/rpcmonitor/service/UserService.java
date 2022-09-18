package com.sheledon.rpcmonitor.service;

import com.sheledon.rpcmonitor.vo.UserVo;

import java.util.List;

/**
 * @author Sheledon
 * @date 2022/2/24
 * @Version 1.0
 */
public interface UserService {
    List<UserVo> getUserList();
    void addUser(UserVo user);
    void deleteUser(String username);
}
