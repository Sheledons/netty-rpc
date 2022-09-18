package com.sheledon.spring;

import com.sheledon.context.RpcContext;
import com.sheledon.monitor.RpcMonitor;
import com.sheledon.transport.netty.server.RpcServer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.UnknownHostException;
import java.util.concurrent.*;


/**
 * 钩子，在spring自动之后启动server
 * @author Sheledon
 * @date 2021/11/18
 * @Version 1.0
 */
@Component
public class RpcServerRunner implements InitializingBean {

    @Autowired
    private RpcServer server;
    @Autowired
    private RpcContext rpcContext;
    @Autowired
    private RpcMonitor rpcMonitor;
    private ThreadPoolExecutor executor;

    public RpcServerRunner() {
        executor = new ThreadPoolExecutor(
                1,1,60, TimeUnit.SECONDS,new ArrayBlockingQueue<>(10));
    }

    /**
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if (!StringUtils.isEmpty(rpcContext.getRpcMonitorUrl())){
            ScheduledExecutorService scheduledService = Executors.newScheduledThreadPool(1);
            scheduledService.scheduleAtFixedRate(rpcMonitor,2,
                    rpcContext.getRpcMonitorPeriod(),TimeUnit.SECONDS);
        }
        if (rpcContext.isRpcServer()){
            executor.execute(()->{
                try {
                    server.start();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
