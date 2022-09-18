package com.example.mapper;

import cn.hutool.core.util.ObjectUtil;
import com.example.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author baisongyuan
 * @className UserMapper
 * @description
 * @date 2022/5/13 8:36
 */
@Repository
public class UserMapper {
    private static Map<Integer, User> USER_MAP = new ConcurrentHashMap<>();

    static {
        for (int i = 0; i < 10; i++) {
            USER_MAP.put(i,User.builder()
                    .id(i)
                    .age(i)
                    .username("admin"+i)
                    .password("pwd"+i)
                    .build());
        }
    }

    public void save(User user){
        USER_MAP.put(user.getId(),user);
    }

    public void update(User user){
        User oldUser = USER_MAP.get(user.getId());
        if (ObjectUtil.isEmpty(oldUser)){
            throw new RuntimeException("user not exists");
        }
        if (!StringUtils.isEmpty(user.getPassword())){
            oldUser.setUsername(user.getUsername());
        }
        if (!StringUtils.isEmpty(user.getUsername())){
            oldUser.setUsername(user.getUsername());
        }
        if (ObjectUtil.isNotEmpty(user.getAge())){
            oldUser.setAge(user.getAge());
        }
    }

    public void delete(Integer id){
        USER_MAP.remove(id);
    }

    public User getById(Integer id){
        return USER_MAP.get(id);
    }
}
