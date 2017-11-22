package com.lawrence.fatalis.aop;

import com.lawrence.fatalis.annotation.ReadDataSource;
import com.lawrence.fatalis.annotation.WriteDataSource;
import com.lawrence.fatalis.datasource.DataSourceContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.PriorityOrdered;

import java.lang.reflect.Method;

/**
 * 切换数据源aop类
 */
@Aspect
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
@Configuration
@ConditionalOnProperty(prefix = "fatalis", name = "multi-datasource-open", havingValue = "true")
public class DataSourceAop implements PriorityOrdered {

    // 有读写注解的service方法, 判断并切换数据源
    @Pointcut(value = "execution(* com.lawrence.fatalis.service.impl..*.*(..)) && " +
            "(@annotation(com.lawrence.fatalis.annotation.WriteDataSource) || " +
            "@annotation(com.lawrence.fatalis.annotation.ReadDataSource))")
    private void isAnnotation() {}

    @Around("isAnnotation()")
    public Object switchDataSource(ProceedingJoinPoint point) throws Throwable {

        Signature signature = point.getSignature();
        MethodSignature methodSignature = null;
        if (!(signature instanceof MethodSignature)) {
            throw new IllegalArgumentException("读写注解只能作用于方法");
        }
        methodSignature = (MethodSignature) signature;

        Object target = point.getTarget();
        Method currentMethod = target.getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());

        WriteDataSource write = currentMethod.getAnnotation(WriteDataSource.class);
        ReadDataSource read = currentMethod.getAnnotation(ReadDataSource.class);

        // 当前方法存在WriteDataSource注解
        if (write != null && read == null) {
            DataSourceContext.setMaster();

        // 当前方法存在ReadDataSource注解
        } else if (write == null && read != null) {
            DataSourceContext.setSlave();
        } else {
            throw new IllegalArgumentException("读写注解不能同时作用于单个方法");
        }

        try {
            return point.proceed();
        } finally {
            DataSourceContext.clear();
        }

    }

    // 无读写注解的service方法, 默认master数据源
    @Pointcut(value = "execution(* com.lawrence.fatalis.service.impl..*.*(..)) && " +
            "(!@annotation(com.lawrence.fatalis.annotation.WriteDataSource) && " +
            "!@annotation(com.lawrence.fatalis.annotation.ReadDataSource))")
    private void isNotAnnotation() {}

    @Around("isNotAnnotation()")
    public Object defaultDataSource(ProceedingJoinPoint point) throws Throwable {
        DataSourceContext.setMaster();

        try {
            return point.proceed();
        } finally {
            DataSourceContext.clear();
        }

    }

    @Override
    public int getOrder() {

        // 在启动类中加上了@EnableTransactionManagement(order = 10)
        return 1;
    }

}
