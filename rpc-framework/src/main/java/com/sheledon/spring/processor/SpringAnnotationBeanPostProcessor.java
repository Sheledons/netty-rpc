package com.sheledon.spring.processor;

import com.sheledon.spring.scanner.RpcClassPathBeanDefinitionScanner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * 扫描spring-Framework中component
 * spring不会主动扫描外部jar下的class的
 * @author Sheledon
 * @date 2021/11/18
 * @Version 1.0
 */
@Slf4j
public class SpringAnnotationBeanPostProcessor implements
        BeanDefinitionRegistryPostProcessor, EnvironmentAware {

    private final Set<String> packagesToScan;
    private final Class<? extends Annotation> [] springAnnotationArray = new Class[]{Component.class, Configuration.class};

    public SpringAnnotationBeanPostProcessor(Set<String> packagesToScan) {
        this.packagesToScan = packagesToScan;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        for (Class<? extends Annotation> annotationClazz : springAnnotationArray){
            RpcClassPathBeanDefinitionScanner scanner =
                    new RpcClassPathBeanDefinitionScanner(registry, annotationClazz);
            scanner.scan(packagesToScan);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    @Override
    public void setEnvironment(Environment environment) {

    }
}
