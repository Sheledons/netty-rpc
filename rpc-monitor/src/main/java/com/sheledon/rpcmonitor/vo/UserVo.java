package com.sheledon.rpcmonitor.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Sheledon
 * @date 2022/2/24
 * @Version 1.0
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserVo {
    private String username;
    private String password;
}
