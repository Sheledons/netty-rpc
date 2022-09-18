package com.sheledon.annotation;

import java.lang.annotation.*;

/**
 * 过滤器注解
 * @author Sheledon
 * @date 2022/2/17
 * @Version 1.0
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RpcFilter {
    /**
     * target表示是在consumer和provider方调用过滤器
     *  PROVIDER = "provider";
     *  CONSUMER = "consumer"
     * @return
     */
    String [] target() default {};
}
