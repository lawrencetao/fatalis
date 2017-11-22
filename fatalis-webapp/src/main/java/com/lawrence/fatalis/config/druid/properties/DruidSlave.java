package com.lawrence.fatalis.config.druid.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class DruidSlave extends DruidMaster {

    /** druid从库配置 */
    private String url;
    private String username;
    private String password;
    private String driverClassName = "com.mysql.jdbc.Driver";
    private Integer initialSize = 2;
    private Integer minIdle = 2;
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
