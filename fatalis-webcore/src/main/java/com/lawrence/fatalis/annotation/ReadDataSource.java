package com.lawrence.fatalis.annotation;

import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

/**
 * 自定义注解, 数据源: 读
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Transactional(readOnly = true)// 读数据源注解设定只读事务
public @interface ReadDataSource {

}
