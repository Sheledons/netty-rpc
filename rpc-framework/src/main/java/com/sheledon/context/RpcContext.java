package com.sheledon.context;

import com.sheledon.common.enums.RpcConfigEnum;
import com.sheledon.common.netty.utils.PropertiesFileUtil;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 保存rpc.properties中的配置信息，同时通过该类也可以查看
 * 项目启动的时候可以配置上面信息
 * @author Sheledon
 * @date 2022/2/18
 * @Version 1.0
 */
@Component
@Getter
public class RpcContext {
    private boolean isRpcServer;
    private boolean isRpcClient;
    private String rpcMonitorUrl;
    private String rpcApplicationName;
    private String rpcConfigPath ;
    private String rpcZookeeperAddress;
    private Integer rpcMonitorPeriod;
    private String loadBalance;

    public RpcContext() {
        setRpcConfigPath();
        setIsRpcServer();
        setIsRpcClient();
        setRpcMonitorUrl();
        setRpcApplicationName();
        setRpcZookeeperAddress();
        setRpcMonitorPeriod();
        setLoadBalance();
    }

    private void setLoadBalance() {
        try {
            this.loadBalance = PropertiesFileUtil.getPropertiesValue(
                    rpcConfigPath,
                    RpcConfigEnum.LOAD_BALANCE.propertyValue()
            );
        }catch (Exception e){
            this.loadBalance = (String) RpcConfigEnum.LOAD_BALANCE.defaultValue();
        }finally {
            if (Objects.isNull(this.loadBalance)){
                this.loadBalance = (String) RpcConfigEnum.LOAD_BALANCE.defaultValue();
            }
        }
    }

    private void setRpcConfigPath(){
        rpcConfigPath = RpcConfigEnum.RPC_CONFIG_PATH.propertyValue();
    }
    private void setIsRpcServer(){
        try {
            isRpcServer = Boolean.parseBoolean(PropertiesFileUtil.getPropertiesValue(
                    rpcConfigPath,
                    RpcConfigEnum.RPC_SERVER.propertyValue()
            ));
        }catch (Exception e){
            isRpcServer = (boolean) RpcConfigEnum.RPC_SERVER.defaultValue();
        }
    }
    private void setIsRpcClient(){
        try {
            isRpcClient = Boolean.parseBoolean(PropertiesFileUtil.getPropertiesValue(
                    rpcConfigPath,
                    RpcConfigEnum.RPC_CLIENT.propertyValue()
            ));
        }catch (Exception e){
            isRpcClient = (boolean) RpcConfigEnum.RPC_CLIENT.defaultValue();
        }
    }
    private void setRpcMonitorUrl(){
        try {
            rpcMonitorUrl = PropertiesFileUtil.getPropertiesValue(
                    rpcConfigPath,
                    RpcConfigEnum.RPC_MONITOR.propertyValue()
            );
        }catch (Exception e){
            rpcMonitorUrl = (String) RpcConfigEnum.RPC_MONITOR.defaultValue();
        }
    }
    private void setRpcApplicationName(){
        try {
            rpcApplicationName = PropertiesFileUtil.getPropertiesValue(
                    rpcConfigPath,
                    RpcConfigEnum.RPC_APPLICATION_NAME.propertyValue()
            );
        }catch (Exception e){
            rpcApplicationName = (String) RpcConfigEnum.RPC_APPLICATION_NAME.defaultValue();
        }
    }
    private void setRpcZookeeperAddress(){
        try {
            rpcZookeeperAddress = PropertiesFileUtil.getPropertiesValue(
                    rpcConfigPath,
                    RpcConfigEnum.ZK_ADDRESS.propertyValue()
            );
        }catch (Exception e){
            rpcZookeeperAddress = (String) RpcConfigEnum.ZK_ADDRESS.defaultValue();
        }
    }
    private void setRpcMonitorPeriod(){
        try {
            rpcMonitorPeriod = Integer.valueOf(PropertiesFileUtil.getPropertiesValue(
                    rpcConfigPath,
                    RpcConfigEnum.RPC_MONITOR_PERIOD.propertyValue()
            ));
        }catch (Exception e){
            rpcMonitorPeriod = (Integer) RpcConfigEnum.RPC_MONITOR_PERIOD.defaultValue();
        }
    }
}
