package com.sheledon.rpcmonitor.controller;

import com.alibaba.fastjson.JSON;
import com.sheledon.common.monitor.entity.LogDto;
import com.sheledon.rpcmonitor.config.Api;
import com.sheledon.rpcmonitor.factory.ResponseFactory;
import com.sheledon.rpcmonitor.service.RpcLogService;
import com.sheledon.rpcmonitor.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping(Api.RpcLog.BASE_URL)
public class RpcLogController {
    @Autowired
    private RpcLogService rpcLogService;

    @GetMapping
    public ResponseVo getLogList(){
        return ResponseFactory.buildSuccessResponse(rpcLogService.getLogList());
    }

    @PostMapping
    public void saveLog(@RequestBody String json){
        List<LogDto> logDtoList = JSON.parseArray(json, LogDto.class);
        rpcLogService.saveLog(logDtoList);
    }
}
