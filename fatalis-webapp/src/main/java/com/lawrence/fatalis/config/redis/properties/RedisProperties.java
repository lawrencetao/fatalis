package com.lawrence.fatalis.config.redis.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * redis配置实体
 */
@Configuration
@ConfigurationProperties(prefix = "fatalis.redis")
@Data
public class RedisProperties {

    /** redis配置参数 */
    private String host;
    private int port;
    private String password;
    private int maxTotal = 20;
    private int maxIdle = 10;
    private int minIdle = 2;
    private int timeout = 30000;

    /** redis-cluster集群配置参数 */
    private RedisCluster cluster;

}

