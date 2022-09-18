package com.sheledon.annotation;

import java.lang.annotation.*;

/**
 * 用于标注提供的服务
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RpcService {
    String group() default "";
    String version() default "";
    String interfaceName() default "";
}
