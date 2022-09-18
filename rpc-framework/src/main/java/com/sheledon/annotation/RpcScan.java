package com.sheledon.annotation;


import com.sheledon.spring.ComponentScanRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 设置扫描路径，应该配置在启动类上面
 * @author Sheledon
 * @date 2021/11/15
 * @Version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({ComponentScanRegistrar.class})
public @interface RpcScan {
    String[] basePackages() default {};
    Class<?> [] basePackageClasses() default {};
}
