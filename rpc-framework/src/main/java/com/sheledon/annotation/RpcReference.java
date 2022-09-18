package com.sheledon.annotation;

import java.lang.annotation.*;

/**
 * 用于表示需要远程调用的服务类
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RpcReference {
    String group() default "";
    String version() default "";
    String interfaceName() default "";
}
