package com.sheledon.spring.scanner;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Set;

/**
 * 自定义的beanDefine扫描器
 * @author Sheledon
 * @date 2021/11/18
 * @Version 1.0
 */
public class RpcClassPathBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {


    public RpcClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry, Class<? extends Annotation> annotation) {
        super(registry);
        super.addIncludeFilter(new AnnotationTypeFilter(annotation));
    }

    @Override
    public int scan(String... basePackages) {
        return super.scan(basePackages);
    }
    public int scan(Collection<String> basePackages){
        String[] strings = new String[basePackages.size()];
        int i = 0 ;
        for (String s : basePackages){
            strings[i++] = s;
        }
        return scan(strings);
    }
}
