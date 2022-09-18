package com.sheledon.spring;

import com.sheledon.annotation.RpcScan;
import com.sheledon.spring.constant.Constants;
import com.sheledon.spring.processor.SpringAnnotationBeanPostProcessor;
import com.sheledon.spring.processor.RpcFilterAnnotationBeanPostProcessor;
import com.sheledon.spring.processor.ServiceAnnotationBeanPostProcessor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.*;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.beans.factory.support.BeanDefinitionBuilder.rootBeanDefinition;

/**
 * @author Sheledon
 * @date 2021/11/18
 * @Version 1.0
 */
public class ComponentScanRegistrar implements ImportBeanDefinitionRegistrar{
    private static final Set<String> RPC_BASE_PACKAGE_SET = new HashSet<>() ;
    static{
        RPC_BASE_PACKAGE_SET.add(Constants.RPC_BASE_PACKAGES);
    }
    /**
     * 这里通过主动扫描，将带有@RpcService的类的beanDefine注册到spring
     * 的defineMap中，然后后面由spring进行实例化
     * @param importingClassMetadata
     * @param registry
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        //获得@Service扫描路径
        Set<String> packagesToScan = getPackagesToScan(importingClassMetadata);
        //注册@RpcService，@RpcReference的beanDefinition
        registerBeanAnnotationBeanPostProcessor(packagesToScan, ServiceAnnotationBeanPostProcessor.class,registry);
        //注册rpc-framework中@component修饰的beanDefinition
        registerBeanAnnotationBeanPostProcessor(RPC_BASE_PACKAGE_SET, SpringAnnotationBeanPostProcessor.class,registry);
        registerBeanAnnotationBeanPostProcessor(RPC_BASE_PACKAGE_SET, RpcFilterAnnotationBeanPostProcessor.class,registry);
    }
    private Set<String> getPackagesToScan(AnnotationMetadata metadata){
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(RpcScan.class.getName()));
        String[] basePackages = attributes.getStringArray("basePackages");
        Class<?>[] basePackageClasses = attributes.getClassArray("basePackageClasses");
        Set<String> packagesToScan = new HashSet<>(Arrays.asList(basePackages));
        for (Class<?> clazz : basePackageClasses){
            packagesToScan.add(ClassUtils.getPackageName(clazz));
        }
        return packagesToScan;
    }
    private void registerBeanAnnotationBeanPostProcessor(Set<String> packagesToScan,Class<? extends BeanDefinitionRegistryPostProcessor> clazz, BeanDefinitionRegistry registry) {
        BeanDefinitionBuilder builder = rootBeanDefinition(clazz);
        builder.addConstructorArgValue(packagesToScan);
        builder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
        BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinition, registry);
    }

}
