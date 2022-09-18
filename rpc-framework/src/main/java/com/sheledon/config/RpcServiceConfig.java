package com.sheledon.config;

import lombok.Builder;
import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * @author Sheledon
 * @date 2021/11/18
 * @Version 1.0
 */
@Builder
@Data
public class RpcServiceConfig {
    private String group;
    private String version;
    private Object service;
    public String getRpcServiceName(){
        Class<?> interClazz = this.service.getClass().getInterfaces()[0];
        String serviceName = interClazz.getName();
        if (StringUtils.hasText(group)){
            serviceName+="/"+group;
        }
        if (StringUtils.hasText(version)){
            serviceName+="/"+group;
        }
        return serviceName;
    }
}
