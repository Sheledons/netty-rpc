package com.sheledon.rpcmonitor.transport.netty.strategy;

import com.sheledon.common.monitor.entity.ApplicationDto;
import com.sheledon.common.monitor.entity.CollectInfo;
import com.sheledon.common.monitor.entity.ServiceInvokeDto;
import com.sheledon.common.netty.entity.RpcMessage;
import com.sheledon.rpcmonitor.service.RpcApplicationService;
import com.sheledon.rpcmonitor.service.RpcLogService;
import com.sheledon.rpcmonitor.service.RpcService;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.SocketAddress;
import java.util.Objects;

/**
 * 处理应用发送过来的信息
 * @author Sheledon
 * @date 2022/2/23
 * @Version 1.0
 */
@Component
public class RpcMonitorCollectStrategy implements MessageStrategy{
    @Autowired
    private RpcApplicationService rpcApplicationService;
    @Autowired
    private RpcLogService rpcLogService;
    @Autowired
    private RpcService rpcService;


    @Override
    public RpcMessage handle(ChannelHandlerContext context, RpcMessage message) {
        CollectInfo collectInfo = (CollectInfo) message.getBody();
        ApplicationDto applicationDto = collectInfo.getApplicationDto();
        if (!Objects.isNull(applicationDto)){
            SocketAddress address = context.channel().remoteAddress();
            System.out.println(address);
            rpcApplicationService.addApplicationAddress(address.toString(), applicationDto.getApplicationName());
            rpcApplicationService.addApplication(applicationDto);
            rpcLogService.saveLog(collectInfo.getLogDtos());
            rpcService.updateServiceInvokeCount(ServiceInvokeDto.builder()
                    .applicationName(applicationDto.getApplicationName())
                    .serviceInvokeCount(collectInfo.getServiceInvokeMap())
                    .build());
        }
        return null;
    }
}
