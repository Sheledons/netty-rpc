package com.sheledon.rpcmonitor.mapper.impl;

import com.sheledon.common.monitor.entity.ApplicationDto;
import com.sheledon.common.monitor.entity.RpcServiceDto;
import com.sheledon.common.monitor.entity.ServiceInvokeDto;
import com.sheledon.rpcmonitor.mapper.ApplicationMapper;
import com.sheledon.rpcmonitor.mapper.RpcServiceMapper;
import com.sheledon.rpcmonitor.vo.RpcServiceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Sheledon
 * @date 2022/2/20
 * @Version 1.0
 */
@Component
public class RpcServiceMapperImpl implements RpcServiceMapper {

    private Map<String, Map<String, AtomicInteger>> serviceInvokeCountMap;
    @Autowired
    private ApplicationMapper applicationMapper;

    public RpcServiceMapperImpl() {
        serviceInvokeCountMap = new ConcurrentHashMap<>();
    }

    @Override
    public void updateServiceInvokeCount(ServiceInvokeDto serviceInvokeDto) {
        if (Objects.isNull(serviceInvokeDto)){
            return;
        }
        serviceInvokeCountMap.put(serviceInvokeDto.getApplicationName(),
                serviceInvokeDto.getServiceInvokeCount());
    }


    @Override
    public List<RpcServiceVo> getRpcServiceByName(String applicationName) {
        ApplicationDto application =applicationMapper.getByName(applicationName);
        List<RpcServiceDto> serviceDtoList = application.getServiceList();
        List<RpcServiceVo> rpcServiceVoList = new LinkedList<>();
        if (Objects.isNull(serviceDtoList)){
            return rpcServiceVoList;
        }
        Map<String, AtomicInteger> serviceInvokeMap = serviceInvokeCountMap.get(applicationName);
        serviceDtoList.forEach(serviceDto->{
            AtomicInteger invokeCount = serviceInvokeMap.get(serviceDto.getServiceName());
            RpcServiceVo rpcServiceVo = RpcServiceVo.builder()
                    .group(serviceDto.getGroup())
                    .version(serviceDto.getVersion())
                    .serviceName(serviceDto.getServiceName())
                    .startTime(application.getTime())
                    .provider(applicationName)
                    .invokeCount(Objects.isNull(invokeCount) ? 0 : invokeCount.intValue())
                    .build();
            rpcServiceVoList.add(rpcServiceVo);
        });
        return rpcServiceVoList;
    }

    @Override
    public List<RpcServiceVo> getRpcService() {
        List<ApplicationDto> applicationList = applicationMapper.getList();
        List<RpcServiceVo> rpcServiceVoList = new LinkedList<>();
        applicationList.forEach(application->{
            rpcServiceVoList.addAll(this.getRpcServiceByName(application.getApplicationName()));
        });
        return rpcServiceVoList;
    }

    @Override
    public int getRpcServiceNumber() {
        List<ApplicationDto> applicationDtos = applicationMapper.getList();
        int result = 0 ;
        for (ApplicationDto applicationDto : applicationDtos) {
            result+=applicationDto.getServiceList().size();
        }
        return result;
    }

    @Override
    public int getServiceInvokeCount() {
        AtomicInteger count = new AtomicInteger();
        Collection<Map<String, AtomicInteger>> values = serviceInvokeCountMap.values();
        for (Map<String, AtomicInteger> map : values) {
            map.forEach((key,value)->{
               count.addAndGet(value.get());
            });
        }
        return count.get();
    }
}
