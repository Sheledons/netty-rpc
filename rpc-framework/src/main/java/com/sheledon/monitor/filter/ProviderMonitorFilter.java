package com.sheledon.monitor.filter;


import com.sheledon.annotation.RpcFilter;
import com.sheledon.common.constants.RpcConstants;
import com.sheledon.context.RpcInvokerContext;
import com.sheledon.monitor.RpcMonitor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * @author Sheledon
 * @date 2022/2/18
 * @Version 1.0
 */
@RpcFilter(target = RpcConstants.PROVIDER)
public class ProviderMonitorFilter implements Filter{
    @Autowired
    private RpcMonitor rpcMonitor;

    @SneakyThrows
    public ProviderMonitorFilter() {}

    @Override
    public void beforeInvoke(RpcInvokerContext context) {
        String serviceName = context.getRequest().getServiceName();
        rpcMonitor.incrementServiceInvokeCount(serviceName);
    }
}
