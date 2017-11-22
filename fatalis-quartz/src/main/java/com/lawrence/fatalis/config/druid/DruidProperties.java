package com.lawrence.fatalis.config.druid;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * druid配置实体
 */
@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
@Data
public class DruidProperties {

    private String url;
    private String username;
    private String password;
    private String driverClassName = "com.mysql.jdbc.Driver";
    private Integer initialSize = 2;
    private Integer minIdle = 1;
    private Integer maxActive = 10;
    private Integer maxWait = 60000;
    private Integer timeBetweenEvictionRunsMillis = 60000;
    private Integer minEvictableIdleTimeMillis = 300000;
    private String validationQuery = "select 1";
    private Boolean testWhileIdle = true;
    private Boolean testOnBorrow = false;
    private Boolean testOnReturn = false;
    private Boolean poolPreparedStatements = true;
    private Integer maxPoolPreparedStatementPerConnectionSize = 20;
    private String filters = "stat,wall,slf4j";
    private String connectionProperties = "druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000";

}

