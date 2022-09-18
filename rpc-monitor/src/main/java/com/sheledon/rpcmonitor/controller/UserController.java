package com.sheledon.rpcmonitor.controller;

import com.sheledon.rpcmonitor.config.Api;
import com.sheledon.rpcmonitor.factory.ResponseFactory;
import com.sheledon.rpcmonitor.service.impl.AdminService;
import com.sheledon.rpcmonitor.vo.ResponseVo;
import com.sheledon.rpcmonitor.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Sheledon
 * @date 2022/2/24
 * @Version 1.0
 */
@RestController
@RequestMapping(Api.User.BASE_URL)
public class UserController {
    @Autowired
    private AdminService adminService;

    @GetMapping(Api.User.USER_LIST)
    public ResponseVo getUserList(){
        return ResponseFactory.buildSuccessResponse(adminService.getUserList());
    }
    @PostMapping
    public ResponseVo saveUser(@RequestBody UserVo userVo){
        adminService.addUser(userVo);
        return ResponseFactory.buildSuccessResponse(null);
    }
    @DeleteMapping(Api.User.DELETE_USER)
    public ResponseVo deleteUser(@PathVariable("name") String name) {
        adminService.deleteUser(name);
        return ResponseFactory.buildSuccessResponse(null);
    }
}
