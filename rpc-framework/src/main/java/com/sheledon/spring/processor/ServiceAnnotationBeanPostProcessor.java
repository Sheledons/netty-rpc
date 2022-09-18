package com.sheledon.spring.processor;

import com.sheledon.annotation.RpcService;
import com.sheledon.spring.scanner.RpcClassPathBeanDefinitionScanner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Sheledon
 * @date 2021/11/18
 * @Version 1.0
 */
@Slf4j
public class ServiceAnnotationBeanPostProcessor implements
        BeanDefinitionRegistryPostProcessor, EnvironmentAware{
    private Environment environment;
    private final Set<String> packagesToScan;

    public ServiceAnnotationBeanPostProcessor(Set<String> packagesToScan) {
        this.packagesToScan = packagesToScan;
    }


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        Set<String> resolvePackagesToScan = resolvePackagesToScan(packagesToScan);
        if (!CollectionUtils.isEmpty(resolvePackagesToScan)){
            registerServiceBeans(resolvePackagesToScan,beanDefinitionRegistry);
        }else{
            log.warn("packagesToScan is empty , ServiceBean registry will be ignored!");
        }
    }

    /**
     * 注册 @Service 的Bean
     * @param packagesToScan
     * @param registry
     */
    private void registerServiceBeans(Set<String> packagesToScan, BeanDefinitionRegistry registry) {
        RpcClassPathBeanDefinitionScanner scanner = new RpcClassPathBeanDefinitionScanner(registry, RpcService.class);
        int scan = scanner.scan(packagesToScan);
        log.info("rpc service number is : {}",scan);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    //解析并且格式化扫描路径
    private Set<String> resolvePackagesToScan(Set<String> packagesToScan){
        Set<String> resolvedPackagesToScan = new LinkedHashSet<String>(packagesToScan.size());
        for (String packageToScan : packagesToScan) {
            if (StringUtils.hasText(packageToScan)) {
                String resolvedPackageToScan = environment.resolvePlaceholders(packageToScan.trim());
                resolvedPackagesToScan.add(resolvedPackageToScan);
            }
        }
        return resolvedPackagesToScan;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

}
