package com.sheledon.rpcmonitor.config.security;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author Sheledon
 * @date 2022/2/24
 * @Version 1.0
 */
@Data
@Builder
public class AdminDetail implements UserDetails {
    private String username;
    private String password;
    private String roles;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String [] authorizes = Objects.isNull(roles) ? new String[]{}: roles.split("\\.");
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (String a : authorizes){
            grantedAuthorities.add(new SimpleGrantedAuthority(a));
        }
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
