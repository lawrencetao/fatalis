package com.lawrence.fatalis.annotation;

import java.lang.annotation.*;

/**
 * 自定义注解, 数据源: 写
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface WriteDataSource {

}
