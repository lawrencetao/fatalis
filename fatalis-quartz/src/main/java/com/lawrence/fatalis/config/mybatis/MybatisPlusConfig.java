package com.lawrence.fatalis.config.mybatis;

import com.alibaba.druid.pool.DruidDataSource;
import com.lawrence.fatalis.config.druid.DruidConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import javax.annotation.Resource;

/**
 * mybatis-plus配置
 */
@Configuration
@MapperScan(basePackages = "com.lawrence.fatalis.dao")
public class MybatisPlusConfig {

    @Resource
    private DruidConfig druidConfig;

    /**
     * 读写分离开关关闭时, 加载数据源代理类
     *
     * @return DruidDataSource
     */
    @Bean
    public DruidDataSource druidDataSource() {

        // 单数据源, master
        DruidDataSource dataSource = new DruidDataSource();
        druidConfig.initDatasource(dataSource);

        return dataSource;
    }

}

