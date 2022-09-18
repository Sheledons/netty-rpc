package com.sheledon.rpcmonitor.config.security;

import com.sheledon.rpcmonitor.service.impl.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Sheledon
 * @date 2022/2/24
 * @Version 1.0
 */
@Service("customUserDetailService")
public class UserDetailService implements UserDetailsService {

    @Autowired
    private AdminService adminService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return adminService.getOneByName(username);
    }
}
