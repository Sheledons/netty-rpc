package com.sheledon.common.enums;

public enum RpcConfigEnum {


    RPC_SERVER("rpc.server",false),
    RPC_CLIENT("rpc.client",false),
    RPC_MONITOR("rpc.monitor.url",""),
    RPC_APPLICATION_NAME("rpc.application.name",""),
    RPC_CONFIG_PATH("rpc.properties",""),
    RPC_MONITOR_PERIOD("rpc.monitor.period",30),
    LOAD_BALANCE("rpc.loadBalance","random"),
    ZK_ADDRESS("rpc.zookeeper.address","");


    private final String propertyValue;

    private Object defaultValue;

    RpcConfigEnum(String propertyValue, Object defaultValue) {
        this.propertyValue = propertyValue;
        this.defaultValue = defaultValue;
    }
    public String propertyValue(){
        return propertyValue;
    }
    public Object defaultValue(){
        return defaultValue;
    }
}
