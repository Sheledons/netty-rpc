package com.sheledon.rpcmonitor.service.impl;

import com.sheledon.common.monitor.entity.ServiceInvokeDto;
import com.sheledon.rpcmonitor.mapper.RpcServiceMapper;
import com.sheledon.rpcmonitor.service.RpcService;
import com.sheledon.rpcmonitor.vo.RpcServiceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Sheledon
 * @date 2022/2/18
 * @Version 1.0
 */
@Service
public class RpcServiceImpl implements RpcService {

    @Autowired
    private RpcServiceMapper rpcServiceMapper;


    @Override
    public void updateServiceInvokeCount(ServiceInvokeDto serviceInvokeDto) {
        rpcServiceMapper.updateServiceInvokeCount(serviceInvokeDto);
    }

    @Override
    public List<RpcServiceVo> getRpcServiceByName(String applicationName) {
        return rpcServiceMapper.getRpcServiceByName(applicationName);
    }

    @Override
    public List<RpcServiceVo> getRpcService() {
        return rpcServiceMapper.getRpcService();
    }

    @Override
    public int getRpcServiceNumber() {
        return rpcServiceMapper.getRpcServiceNumber();
    }
}
