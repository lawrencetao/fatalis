package com.lawrence.fatalis.redis;

import com.alibaba.fastjson.JSON;
import com.lawrence.fatalis.util.StringUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;
import java.util.List;

/**
 * redis-cluster操作类
 */
/*@Component*/
@ConditionalOnClass(JedisCluster.class)
public class ClusterOperator {

    private final static String VIRTUAL_KEY_PREFIX = "fatalis-webapp.cluster.";

    @Resource
    protected JedisCluster jedisCluster;

    /**
     * 对key增加前缀
     *
     * @param key
     * @return String
     */
    private String prefix2Key(final String key) {
        if (StringUtil.isNull(key)) {
            try {
                throw new Exception("Cluster key不能为空");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return VIRTUAL_KEY_PREFIX + key;
    }

    /**
     * 删除key对应值
     *
     * @param key
     */
    public void del(final String key) {
        jedisCluster.del(prefix2Key(key));
    }

    /**
     * 设临时值, 指定保留时间, 单位秒
     *
     * @param key, value, liveTime
     */
    public void set(final String key, final String value, final long liveTime) {
        del(key);
        if (liveTime > 0) {
            jedisCluster.set(prefix2Key(key), value, "NX", "EX", liveTime);
        } else {
            jedisCluster.set(prefix2Key(key), value);
        }
    }

    /**
     * 设永久值
     *
     * @param key, value, liveTime
     */
    public void set(final String key, final String value) {
        jedisCluster.set(prefix2Key(key), value);
    }

    /**
     * 获取key对应的值
     *
     * @param key
     * @return String
     */
    public String get(final String key) {

        return jedisCluster.get(prefix2Key(key));
    }

    /**
     * 设定临时对象, 单位秒
     *
     * @param key, t, liveTime
     */
    public <T> void setObject(String key, T t, final long liveTime) {
        del(key);
        if (liveTime > 0) {
            jedisCluster.set(prefix2Key(key), JSON.toJSONString(t), "NX", "EX", liveTime);
        } else {
            jedisCluster.set(prefix2Key(key), JSON.toJSONString(t));
        }
    }

    /**
     * 设定永久对象
     *
     * @param key, t, liveTime
     */
    public <T> void setObject(String key, T t) {
        jedisCluster.set(prefix2Key(key), JSON.toJSONString(t));
    }

    /**
     * 获取对象
     *
     * @param key
     * @return T
     */
    @SuppressWarnings("unchecked")
    public <T> T getObject(String key, Class clz) {

        return (T) JSON.parseObject(jedisCluster.get(prefix2Key(key)), clz);
    }

    /**
     * 设定临时list值, 单位秒
     *
     * @param key, list, liveTime
     */
    public <T> void setList(String key, List<T> list, final long liveTime) {
        del(key);
        if (liveTime > 0) {
            jedisCluster.set(prefix2Key(key), JSON.toJSONString(list), "NX", "EX", liveTime);
        } else {
            jedisCluster.set(prefix2Key(key), JSON.toJSONString(list));
        }
    }

    /**
     * 设定永久list值
     *
     * @param key, list
     */
    public <T> void setList(String key, List<T> list) {
        jedisCluster.set(prefix2Key(key), JSON.toJSONString(list));
    }

    /**
     * 获取list
     *
     * @param key
     * @return List<T>
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getList(String key, Class clz) {

        return (List<T>) JSON.parseArray(jedisCluster.get(prefix2Key(key)), clz);
    }

    /**
     * 将对象存入list, leftPush
     *
     * @param key
     */
    public <T> void leftPushList(String key, T t) {
        jedisCluster.lpush(prefix2Key(key), JSON.toJSONString(t));
    }

    /**
     * 将对象从list取出, rightPop
     *
     * @param key
     * @return T
     */
    @SuppressWarnings("unchecked")
    public <T> T rightPopList(String key, Class clz) {

        return (T) JSON.parseObject(jedisCluster.rpop(prefix2Key(key)), clz);
    }

    /**
     * 获取当前list的长度
     *
     * @param key
     * @return long
     */
    public long listSize(String key) {

        return jedisCluster.llen(prefix2Key(key));
    }

    /**
     * 设定hash类型的值
     *
     * @param key
     */
    public <T> void setHash(String key, String tKey, T t) {
        jedisCluster.hset(prefix2Key(key), tKey, JSON.toJSONString(t));
    }

    /**
     * 获取hash表的值
     *
     * @param key
     * @return T
     */
    @SuppressWarnings("unchecked")
    public <T> T getHash(String key, String tKey, Class clz) {

        return (T) JSON.parseObject(jedisCluster.hget(prefix2Key(key), tKey), clz);
    }

}
