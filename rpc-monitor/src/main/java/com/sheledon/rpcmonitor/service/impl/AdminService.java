package com.sheledon.rpcmonitor.service.impl;

import com.sheledon.rpcmonitor.config.security.AdminDetail;
import com.sheledon.rpcmonitor.service.UserService;
import com.sheledon.rpcmonitor.vo.UserVo;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Sheledon
 * @date 2022/2/24
 * @Version 1.0
 */
@Service
public class AdminService implements UserService {
    /**
     * 模拟数据库
     */
    private final Map<String, AdminDetail> adminDetailMap = new ConcurrentHashMap<>();

    {
        adminDetailMap.put("admin", AdminDetail.builder()
                .username("admin")
                .password("123456")
                .build());
    }

    public AdminDetail getOneByName(String name){
        return adminDetailMap.get(name);
    }

    @Override
    public List<UserVo> getUserList() {
        List<UserVo> userList = new LinkedList<>();
        adminDetailMap.values().forEach(admin->{
            userList.add(UserVo.builder()
                    .username(admin.getUsername())
                    .password(admin.getPassword())
                    .build());
        });
        return userList;
    }

    @Override
    public void addUser(UserVo user) {
        AdminDetail adminDetail = AdminDetail.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
        adminDetailMap.put(user.getUsername(),adminDetail);
    }

    @Override
    public void deleteUser(String username) {
        adminDetailMap.remove(username);
    }

}
