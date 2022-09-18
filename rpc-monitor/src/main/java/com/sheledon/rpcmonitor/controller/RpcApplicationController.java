package com.sheledon.rpcmonitor.controller;

import com.alibaba.fastjson.JSON;
import com.sheledon.common.monitor.entity.ApplicationDto;
import com.sheledon.rpcmonitor.config.Api;
import com.sheledon.rpcmonitor.factory.ResponseFactory;
import com.sheledon.rpcmonitor.service.RpcApplicationService;
import com.sheledon.rpcmonitor.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Sheledon
 * @date 2022/2/17
 * @Version 1.0
 */
@RestController
@RequestMapping(Api.RpcApplication.BASE_URL)
public class RpcApplicationController {
    @Autowired
    private RpcApplicationService rpcApplicationService;

    @PostMapping()
    public void saveApplication(@RequestBody String json){
        ApplicationDto application = JSON.parseObject(json, ApplicationDto.class);
        rpcApplicationService.addApplication(application);
    }
    @GetMapping()
    public ResponseVo getApplicationList(){
        return ResponseFactory.buildSuccessResponse(rpcApplicationService.getApplicationList());
    }
    @GetMapping(Api.RpcApplication.GET_APPLICATION)
    public ResponseVo getApplication(@PathVariable("name") String applicationName){
        return ResponseFactory.buildSuccessResponse(rpcApplicationService.getApplicationByName(applicationName));
    }
}
