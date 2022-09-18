package com.sheledon.spring.processor;

import com.sheledon.annotation.RpcFilter;
import com.sheledon.annotation.RpcReference;
import com.sheledon.annotation.RpcService;
import com.sheledon.config.RpcServiceConfig;
import com.sheledon.common.constants.RpcConstants;
import com.sheledon.context.RpcContext;
import com.sheledon.common.netty.factory.SingletonFactory;
import com.sheledon.monitor.filter.Filter;
import com.sheledon.provider.ServiceProvider;
import com.sheledon.provider.impl.ZkServiceProviderImpl;
import com.sheledon.proxy.RpcClientCglibProxy;
import com.sheledon.proxy.RpcClientProxy;
import com.sheledon.transport.RpcRequestTransport;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 自定义的bean处理器，spring在将bean实例化之后会调用该处理器
 * 这里主要负责服务注册，代理类生成，过滤类的收集等
 * @author Sheledon
 * @date 2021/11/18
 * @Version 1.0
 */
@Component
@Slf4j
public class SpringBeanProcessor implements BeanPostProcessor {

    @Autowired
    private RpcRequestTransport rpcClient;

    private final ServiceProvider serviceProvider;

    @Autowired
    private RpcContext rpcContext;

    private static final List<Filter> CONSUMER_FILTER_LIST = new LinkedList<>();
    private static final List<Filter> PROVIDER_FILTER_LIST = new LinkedList<>();
    public SpringBeanProcessor() {
        serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);
    }
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * 1. 向注册中心注册service
     * 2. 生成代理类，注入bean中
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        injectRpcProxy(bean);
        if (clazz.isAnnotationPresent(RpcService.class)){
            log.info("add rpcService: {}",bean);
            addRpcService(bean);
        }
        if (!StringUtils.isEmpty(rpcContext.getRpcMonitorUrl())
                && clazz.isAnnotationPresent(RpcFilter.class)){
            addFilter(bean,clazz);
        }
        return bean;
    }
    private void addRpcService(Object bean){
        Class<?> beanClass = bean.getClass();
        RpcService annotation = beanClass.getAnnotation(RpcService.class);
        RpcServiceConfig serviceConfig = RpcServiceConfig.builder()
                .group(annotation.group())
                .version(annotation.version())
                .service(bean)
                .build();
        serviceProvider.publishService(serviceConfig);
    }
    @SneakyThrows
    private void injectRpcProxy(Object bean){
        Class<?> clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(RpcReference.class)){
                RpcReference annotation = field.getAnnotation(RpcReference.class);
                RpcServiceConfig serviceConfig = RpcServiceConfig.builder()
                        .group(annotation.group())
                        .version(annotation.version())
                        .build();
                RpcClientProxy clientProxy = new RpcClientProxy(rpcClient, serviceConfig,rpcContext);
                Object proxy = clientProxy.getProxy(field.getType());
//                RpcClientCglibProxy clientCglibProxy = new RpcClientCglibProxy(rpcClient, serviceConfig, rpcContext);
//                Object proxy = clientCglibProxy.getProxy(field.getType());
                field.setAccessible(true);
                field.set(bean,proxy);
            }
        }
    }

    private void addFilter(Object bean,Class<?> clazz){
        RpcFilter annotation = clazz.getAnnotation(RpcFilter.class);
        String[] targets = annotation.target();
        Arrays.stream(targets).forEach(target->{
            if (RpcConstants.CONSUMER.equals(target)){
                CONSUMER_FILTER_LIST.add((Filter) bean);
            }
            if (RpcConstants.PROVIDER.equals(target)){
                PROVIDER_FILTER_LIST.add((Filter) bean);
            }
        });
    }

    public static List<Filter> getConsumerFilterList() {
        return CONSUMER_FILTER_LIST;
    }

    public static List<Filter> getProviderFilterList() {
        return PROVIDER_FILTER_LIST;
    }
}