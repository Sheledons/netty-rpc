package com.sheledon.spring.processor;

import com.sheledon.annotation.RpcFilter;
import com.sheledon.spring.scanner.RpcClassPathBeanDefinitionScanner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.util.Set;

/**
 * 扫描filter
 * @author Sheledon
 * @date 2022/2/17
 * @Version 1.0
 */
@Slf4j
public class RpcFilterAnnotationBeanPostProcessor implements
        BeanDefinitionRegistryPostProcessor, EnvironmentAware {

    private final Set<String> packagesToScan;

    public RpcFilterAnnotationBeanPostProcessor(Set<String> packagesToScan) {
        this.packagesToScan = packagesToScan;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        RpcClassPathBeanDefinitionScanner scanner =
                new RpcClassPathBeanDefinitionScanner(registry,RpcFilter.class);
        int scan = scanner.scan(packagesToScan);
        log.info("rpc-framework @filter Bean number is {}",scan);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    @Override
    public void setEnvironment(Environment environment) {

    }
}
