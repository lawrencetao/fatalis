package com.lawrence.fatalis.config.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lawrence.fatalis.config.redis.properties.RedisCluster;
import com.lawrence.fatalis.config.redis.properties.RedisProperties;
import com.lawrence.fatalis.util.StringUtil;
import com.lawrence.fatalis.util.rsa7des.AESCoder;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
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

@Configuration
public class RedisConfig {

    @Resource
    private RedisProperties redisProperties;

    /**
     * 集群开关关闭时, 加载jedisConnectionFactory实例
     *
     * @return RedisConnectionFactory
     */
    @Bean
    @ConditionalOnProperty(prefix = "fatalis", name = "redis-cluster-open", havingValue = "false")
    public RedisConnectionFactory redisConnectionFactory(){
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(redisProperties.getMaxTotal());
        poolConfig.setMaxIdle(redisProperties.getMaxIdle());
        poolConfig.setMinIdle(redisProperties.getMinIdle());

        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(poolConfig);
        jedisConnectionFactory.setHostName(redisProperties.getHost());

        String password = redisProperties.getPassword();

        if(StringUtil.isNotNull(password)){

            // 配置文件中密码进行解密
            /*try {
                password = AESCoder.decrypt(password, AESCoder.CONFIG_KEY);
            } catch (Exception e) {
                e.printStackTrace();
            }*/

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

    /**
     * 集群开关打开时, 加载jedisCluster实例
     *
     * @return JedisCluster
     */
    @Bean
    @ConditionalOnProperty(prefix = "fatalis", name = "redis-cluster-open", havingValue = "true")
    public JedisCluster redisCluster() {
        Set<HostAndPort> nodes = new HashSet<>();

        // cluster集群参数
        RedisCluster cluster = redisProperties.getCluster();

        // 连接池最大连接, 最大空闲, 最小空闲,
        GenericObjectPoolConfig jedisPool = new GenericObjectPoolConfig();
        jedisPool.setMaxTotal(cluster.getMaxTotal());
        jedisPool.setMaxIdle(cluster.getMaxIdle());
        jedisPool.setMinIdle(cluster.getMinIdle());

        for (String node : cluster.getNodes()) {
            String[] parts = StringUtil.split(node, ":");
            Assert.state(parts.length == 2, "Cluster node shoule be defined as 'host:port', not '" + Arrays.toString(parts) + "'");
            nodes.add(new HostAndPort(parts[0], Integer.valueOf(parts[1])));
        }

        String password = cluster.getPassword();

        // 根据密码配置, 加载不同的jedisCluster构造
        if (StringUtil.isNotNull(password)) {

            // 配置文件中密码进行解密
            /*try {
                password = AESCoder.decrypt(password, AESCoder.CONFIG_KEY);
            } catch (Exception e) {
                e.printStackTrace();
            }*/

            return new JedisCluster(nodes, cluster.getTimeout(), cluster.getClusterTimeout(), cluster.getClusterMaxAttempts(), password, jedisPool);
        } else {

            return new JedisCluster(nodes, cluster.getTimeout(), cluster.getClusterTimeout(), cluster.getClusterMaxAttempts(), jedisPool);
        }
    }

}