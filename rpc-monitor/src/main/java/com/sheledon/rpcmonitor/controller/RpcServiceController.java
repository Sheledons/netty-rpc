package com.sheledon.rpcmonitor.controller;

import com.alibaba.fastjson.JSON;
import com.sheledon.common.monitor.entity.ServiceInvokeDto;
import com.sheledon.rpcmonitor.config.Api;
import com.sheledon.rpcmonitor.factory.ResponseFactory;
import com.sheledon.rpcmonitor.service.RpcService;
import com.sheledon.rpcmonitor.vo.ResponseVo;
import com.sheledon.rpcmonitor.vo.RpcServiceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * @author Sheledon
 * @date 2022/2/18
 * @Version 1.0
 */
@RestController
@RequestMapping(Api.RpcService.BASE_URL)
public class RpcServiceController {

    @Autowired
    private RpcService rpcService;

    @PostMapping(Api.RpcService.SERVICE_INVOKE_COU9NT)
    public void saveServiceInvokeCount(@RequestBody String json){
        ServiceInvokeDto serviceInvokeDto = JSON.parseObject(json, ServiceInvokeDto.class);
        rpcService.updateServiceInvokeCount(serviceInvokeDto);
    }
    @GetMapping(Api.RpcService.GET_SERVICE_BY_APPLICATION_NAME)
    public ResponseVo getRpcServiceByName(@PathVariable("applicationName") String applicationName){
        List<RpcServiceVo> rpcServiceVos = rpcService.getRpcServiceByName(applicationName);
        return ResponseFactory.buildSuccessResponse(rpcServiceVos);
    }
    @GetMapping
    public ResponseVo getRpcService(){
        return ResponseFactory.buildSuccessResponse(rpcService.getRpcService());
    }
    @GetMapping(Api.RpcService.SERVICE_NUMBER)
    public ResponseVo getRpcServiceNumber(){
        return ResponseFactory.buildSuccessResponse(rpcService.getRpcServiceNumber());
    }
}
