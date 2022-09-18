package com.example.client.service;

import com.example.api.UserService;
import com.example.entity.User;
import com.sheledon.annotation.RpcReference;
import org.springframework.stereotype.Service;

/**
 * @author Sheledon
 * @date 2021/11/20
 * @Version 1.0
 */
@Service
public class HelloService {

    @RpcReference
    private UserService userService;

    public void show(int id){
        System.out.println("show: "+userService.get(id));
    }

    public void save(User user){
        userService.save(user);
    }

    public void update(User user){
       userService.updateById(user);
    }

    public void delete(int id){
        userService.deleteById(id);
    }
}
