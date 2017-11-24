package com.lawrence.fatalis.config.redis.properties;

import lombok.Data;

import java.util.List;

@Data
public class RedisCluster {

    /** redis-cluster集群配置参数 */
    private List<String> nodes;
    private String password;
    private int clusterMaxRedirects = 5;// 最多尝试次数
    private int clusterTimeout = 30000;// 节点间超时时间
    private int maxTotal = 20;
    private int maxIdle = 10;
    private int minIdle = 2;
    private int timeout = 30000;

}