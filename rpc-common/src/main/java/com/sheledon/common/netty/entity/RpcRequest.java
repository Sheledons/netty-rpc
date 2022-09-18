package com.sheledon.common.netty.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/*
 * @Author sheledon
 * @Date 2021/11/5 上午9:35
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RpcRequest implements Serializable {
    private String applicationName;
    private String group;
    private String version;
    private String requestId;
    private String interfaceName;
    private String methodName;
    private Class<?> [] parametersType;
    private Object[] parameters;

    public String getServiceName(){
        String serviceName = this.interfaceName;
        if (StringUtils.hasText(this.group)){
            serviceName+="/"+this.group;
        }
        if (StringUtils.hasText(this.version)){
            serviceName+="/"+this.group;
        }
        return serviceName;
    }
}
