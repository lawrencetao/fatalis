package com.lawrence.fatalis.config.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lawrence.fatalis.config.redis.properties.RedisCluster;
import com.lawrence.fatalis.config.redis.properties.RedisProperties;
import com.lawrence.fatalis.util.StringUtil;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.Assert;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * redis和cluster集群配置
 */
@Configuration
public class RedisConfig {

    @Resource
    private RedisProperties redisProperties;

    /**
     * 集群开关关闭时, 加载redisConnectionFactory实例
     *
     * @return JedisConnectionFactory
     */
    @Bean
    @ConditionalOnProperty(prefix = "fatalis", name = "redis-cluster-open", havingValue = "false")
    public RedisConnectionFactory redisConnectionFactory(){

        // redis设置jedisPool配置
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(redisProperties.getMaxTotal());
        poolConfig.setMaxIdle(redisProperties.getMaxIdle());
        poolConfig.setMinIdle(redisProperties.getMinIdle());

        // redis设置jedisConnectionFactory配置
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(poolConfig);
        jedisConnectionFactory.setHostName(redisProperties.getHost());
        String password = redisProperties.getPassword();

        if(StringUtil.isNotNull(password)){
            jedisConnectionFactory.setPassword(password);
        }
        jedisConnectionFactory.setPort(redisProperties.getPort());
        jedisConnectionFactory.setTimeout(redisProperties.getTimeout());

        return jedisConnectionFactory;
    }

    /**
     * 集群开关关闭时, 设置redisTemplate序列化
     *
     * @return RedisTemplate
     */
    @Bean
    @ConditionalOnProperty(prefix = "fatalis", name = "redis-cluster-open", havingValue = "false")
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 使用Jackson2JsonRedisSerialize序列化
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        // 设置key, value的序列化规则
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }



    /* ****************************** redis/cluster配置分界线 ****************************** */



    /**
     * 集群开关打开时, 加载JedisPoolConfig实例
     *
     * @return JedisPoolConfig
     */
    @Bean
    @ConditionalOnProperty(prefix = "fatalis", name = "redis-cluster-open", havingValue = "true")
    public JedisPoolConfig clusterJoolConfig() {
        RedisCluster cluster = redisProperties.getCluster();

        // cluster集群jedisPool配置
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(cluster.getMaxTotal());
        poolConfig.setMaxIdle(cluster.getMaxIdle());
        poolConfig.setMinIdle(cluster.getMinIdle());

        return poolConfig;
    }

    /**
     * 集群开关打开时, 加载RedisClusterConfiguration实例
     *
     * @return RedisClusterConfiguration
     */
    @Bean
    @ConditionalOnProperty(prefix = "fatalis", name = "redis-cluster-open", havingValue = "true")
    public RedisClusterConfiguration redisClusterConfiguration() {
        RedisCluster cluster = redisProperties.getCluster();

        // cluster集群redisCluster配置
        RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration();

        // 遍历cluster的nodes集合, 设置nodes
        Set<RedisNode> nodes = new HashSet<>();
        for (String node : cluster.getNodes()) {
            String[] hostAndPort = StringUtil.split(node, ":");
            Assert.state(hostAndPort.length == 2, "RedisCluster集群节点必须定义为: \"host:port\", 而不是: \"" + Arrays.toString(hostAndPort) + "\"");

            nodes.add(new RedisNode(hostAndPort[0], Integer.valueOf(hostAndPort[1])));
        }
        clusterConfig.setClusterNodes(nodes);
        clusterConfig.setMaxRedirects(cluster.getClusterMaxRedirects());

        return clusterConfig;
    }

    /**
     * 集群开关打开时, 加载JedisConnectionFactory实例
     *
     * @param clusterConfig, poolConfig
     * @return JedisConnectionFactory
     */
    @Bean
    @ConditionalOnProperty(prefix = "fatalis", name = "redis-cluster-open", havingValue = "true")
    public JedisConnectionFactory redisClusterConnectionFactory(RedisClusterConfiguration clusterConfig, JedisPoolConfig poolConfig) {
        RedisCluster cluster = redisProperties.getCluster();

        // cluster集群jedisConnectionFactory配置
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(clusterConfig, poolConfig);
        String password = cluster.getPassword();

        if (StringUtil.isNotNull(password)) {
            jedisConnectionFactory.setPassword(password);
        }
        jedisConnectionFactory.setTimeout(cluster.getTimeout());

        return jedisConnectionFactory;
    }

    /**
     * 集群开关打开时, 加载jedisCluster实例
     *
     * @param clusterConfig, poolConfig
     * @return JedisCluster
     */
    @Bean
    @ConditionalOnProperty(prefix = "fatalis", name = "redis-cluster-open", havingValue = "true")
    public JedisCluster jedisCluster(RedisClusterConfiguration clusterConfig, GenericObjectPoolConfig poolConfig) {
        RedisCluster cluster = redisProperties.getCluster();

        Set<HostAndPort> hostAndPort = new HashSet<>();
        for (RedisNode node : clusterConfig.getClusterNodes()) {
            hostAndPort.add(new HostAndPort(node.getHost(), node.getPort()));
        }
        int connectTimeout = cluster.getTimeout();
        int clusterTimeout = cluster.getClusterTimeout();
        String password = cluster.getPassword();

        // 根据密码配置, 加载不同的jedisCluster构造
        if (StringUtil.isNotNull(password)) {

            return new JedisCluster(hostAndPort, connectTimeout, clusterTimeout, clusterConfig.getMaxRedirects(), password, poolConfig);
        } else {

            return new JedisCluster(hostAndPort, connectTimeout, clusterTimeout, clusterConfig.getMaxRedirects(), poolConfig);
        }
    }

}