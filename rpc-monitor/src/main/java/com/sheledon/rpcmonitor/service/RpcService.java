package com.sheledon.rpcmonitor.service;

import com.sheledon.common.monitor.entity.ServiceInvokeDto;
import com.sheledon.rpcmonitor.vo.RpcServiceVo;

import java.util.List;

/**
 * @author Sheledon
 * @date 2022/2/18
 * @Version 1.0
 */
public interface RpcService {
    void updateServiceInvokeCount(ServiceInvokeDto serviceInvokeDto);
    List<RpcServiceVo> getRpcServiceByName(String applicationName);
    List<RpcServiceVo> getRpcService();
    int getRpcServiceNumber();
}
