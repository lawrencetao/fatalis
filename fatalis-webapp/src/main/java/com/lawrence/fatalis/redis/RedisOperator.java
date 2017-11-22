package com.lawrence.fatalis.redis;

import com.lawrence.fatalis.util.StringUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * redis操作类
 */
@Component
@ConditionalOnClass(RedisTemplate.class)
public class RedisOperator {

    private final static String VIRTUAL_KEY_PREFIX = "fatalis-webapp.";

    @Resource
    protected RedisTemplate<String, Object> redisTemplate;

    /**
     * 对key增加前缀
     *
     * @param key
     * @return String
     */
    private String prefix2Key(final String key) {
        if (StringUtil.isNull(key)) {
            try {
                throw new Exception("Redis key不能为空");
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
        redisTemplate.delete(prefix2Key(key));
    }

    /**
     * 设临时值, 指定保留时间, 单位秒
     *
     * @param key, value, liveTime
     */
    public void set(final String key, final String value, final long liveTime) {
        del(key);
        if (liveTime > 0) {
            redisTemplate.opsForValue().set(prefix2Key(key), value, liveTime, TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().set(prefix2Key(key), value);
        }
    }

    /**
     * 设永久值
     *
     * @param key, value
     */
    public void set(final String key, final String value) {
        redisTemplate.opsForValue().set(prefix2Key(key), value);
    }

    /**
     * 获取key对应的值
     *
     * @param key
     * @return String
     */
    public String get(final String key) {

        return (String) redisTemplate.opsForValue().get(prefix2Key(key));
    }

    /**
     * 设定临时对象, 单位秒
     *
     * @param key, t, liveTime
     */
    public <T> void setObject(String key, T t, final long liveTime) {
        del(key);
        if (liveTime > 0) {
            redisTemplate.opsForValue().set(prefix2Key(key), t, liveTime, TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().set(prefix2Key(key), t);
        }
    }

    /**
     * 设定永久对象
     *
     * @param key, t, liveTime
     */
    public <T> void setObject(String key, T t) {
        redisTemplate.opsForValue().set(prefix2Key(key), t);
    }

    /**
     * 获取对象
     *
     * @param key
     * @return T
     */
    @SuppressWarnings("unchecked")
    public <T> T getObject(String key) {

        return (T) redisTemplate.opsForValue().get(prefix2Key(key));
    }

    /**
     * 设定临时list值, 单位秒
     *
     * @param key, list, liveTime
     */
    public <T> void setList(String key, List<T> list, final long liveTime) {
        del(key);
        if (liveTime > 0) {
            redisTemplate.opsForValue().set(prefix2Key(key), list, liveTime, TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().set(prefix2Key(key), list);
        }

    }

    /**
     * 设定永久list值
     *
     * @param key, list
     */
    public <T> void setList(String key, List<T> list) {
        redisTemplate.opsForValue().set(prefix2Key(key), list);
    }

    /**
     * 获取list
     *
     * @param key
     * @return List<T>
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getList(String key) {

        return (List<T>) redisTemplate.opsForValue().get(prefix2Key(key));
    }


    /**
     * 将对象存入list, leftPush
     *
     * @param key
     */
    public <T> void leftPushList(String key, T t) {
        redisTemplate.opsForList().leftPush(prefix2Key(key), t);
    }

    /**
     * 将对象从list取出, rightPop
     *
     * @param key
     * @return T
     */
    @SuppressWarnings("unchecked")
    public <T> T rightPopList(String key) {

        return (T) redisTemplate.opsForList().rightPop(prefix2Key(key));
    }

    /**
     * 获取当前list的长度
     *
     * @param key
     * @return long
     */
    public long listSize(String key) {

        return redisTemplate.opsForList().size(prefix2Key(key));
    }

    /**
     * 设定hash类型的值
     *
     * @param key
     */
    public <T> void setHash(String key, String tKey, T t) {
        redisTemplate.opsForHash().put(prefix2Key(key), tKey, t);
    }

    /**
     * 获取hash表的值
     *
     * @param key
     * @return T
     */
    @SuppressWarnings("unchecked")
    public <T> T getHash(String key, String tKey) {

        return (T) redisTemplate.opsForHash().get(prefix2Key(key), tKey);
    }

}
