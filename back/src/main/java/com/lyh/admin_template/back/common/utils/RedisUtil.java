package com.lyh.admin_template.back.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis 工具类
 */
@Component
public class RedisUtil {

    /**
     * 用于操作 redis
     */
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    /**
     * 用于操作 String
     */
    @Autowired
    private ValueOperations<String, String> valueOperations;
    /**
     * 用于操作 hash
     */
    @Autowired
    private HashOperations<String, String, Object> hashOperations;
    /**
     * 用于操作 list
     */
    @Autowired
    private ListOperations<String, Object> listOperations;
    /**
     * 用于操作 set
     */
    @Autowired
    private SetOperations<String, Object> setOperations;
    /**
     * 用于操作 zset
     */
    @Autowired
    private ZSetOperations<String, Object> zSetOperations;

    /* ================================ common ================================== */
    /**
     * 设置默认过期时间（一天）
     */
    public final static long DEFAULT_EXPIRE = 60 * 60 * 24;
    /**
     * 不设置过期时间
     */
    public final static long NOT_EXPIRE = -1;

    /**
     * 设置 key 的过期时间(单位为 秒)
     */
    public void expire(String key, long expire) {
        redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    /**
     * 根据 key 获取过期时间
     */
    public Long getExpire(String key) {
        return StringUtils.isNotEmpty(key) ? redisTemplate.getExpire(key, TimeUnit.SECONDS) : null;
    }

    /**
     * 根据 pattern 返回匹配的所有 key
     */
    public Set<String> keys(String pattern) {
        return StringUtils.isNotEmpty(pattern) ? redisTemplate.keys(pattern) : null;
    }

    /**
     * 删除 一个 或 多个 key
     */
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 判断 key 是否存在
     */
    public Boolean hasKey(String key) {
        return StringUtils.isNotEmpty(key) ? redisTemplate.hasKey(key) : null;
    }
    /* ================================ common ================================== */

    /* ================================ string ================================== */
    /**
     * string set 操作，设置 key-value
     */
    public void set(String key, String value) {
        set(key, value, DEFAULT_EXPIRE);
    }

    /**
     * string set 操作，设置过期时间
     */
    public void set(String key, String value, long expire) {
        valueOperations.set(key, value);
        if (expire != NOT_EXPIRE) {
            expire(key, expire);
        }
    }

    /**
     * string get 操作，获取 value
     */
    public String get(String key) {
        return StringUtils.isNotEmpty(key) ? valueOperations.get(key) : null;
    }

    /**
     * string incr 操作，加法
     */
    public Long incr(String key, long data) {
        if (data < 0) {
            throw new RuntimeException("请输入正整数");
        }
        return valueOperations.increment(key, data);
    }

    /**
     * string decr 操作，减法
     */
    public Long decr(String key, long data) {
        if (data < 0) {
            throw new RuntimeException("请输入正整数");
        }
        return valueOperations.decrement(key, data);
    }
    /* ================================ string ================================== */

    /* ================================ hash ================================== */
    /**
     * hash set 操作，设置 一个 field - value
     */
    public void hset(String key, String field, Object value) {
        hset(key, field, value, DEFAULT_EXPIRE);
    }

    /**
     * hash set 操作，设置过期时间
     */
    public void hset(String key, String field, Object value, long expire) {
        hashOperations.put(key, field, value);
        if (expire != NOT_EXPIRE) {
            expire(key, expire);
        }
    }

    /**
     * hash set 操作，设置 多个 field - value
     */
    public void hmset(String key, Map<String, Object> map) {
        hmset(key, map, DEFAULT_EXPIRE);
    }

    /**
     * hash set 操作，设置过期时间
     * 此处过期时间是对 key 设置，而非 field（即 key 过期，所有的 field 均失效）。
     */
    public void hmset(String key, Map<String, Object> map, long expire) {
        hashOperations.putAll(key, map);
        if (expire != NOT_EXPIRE) {
            expire(key, expire);
        }
    }

    /**
     * hash keys 操作，获取所有的 field
     */
    public Set<String> hkeys(String key) {
        return StringUtils.isNotEmpty(key) ? hashOperations.keys(key) : null;
    }

    /**
     * hash exists 操作，判断 keys 中是否存在某个 field
     */
    public Boolean hHasKey(String key, String field) {
        if (StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(field)) {
            return hashOperations.hasKey(key, field);
        }
        return null;
    }

    /**
     * hash del 操作，删除一个 或 多个 field
     */
    public void hdel(String key, String... field) {
        if (StringUtils.isNotEmpty(key) && field.length > 0) {
            hashOperations.delete(key, field);
        }
    }

    /**
     * hash get 操作，获取一个 field 对应的 value 值
     */
    public Object hget(String key, String field) {
        if (StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(field)) {
            return hashOperations.get(key, field);
        }
        return null;
    }

    /**
     * hash get 操作，获取所有的 value 值
     */
    public List<Object> hmget(String key) {
        return StringUtils.isNotEmpty(key) ? hashOperations.values(key) : null;
    }

    /**
     * hash get 操作，获取所有的 field-value
     */
    public Map<String, Object> hgetAll(String key) {
        return StringUtils.isNotEmpty(key) ? hashOperations.entries(key) : null;
    }
    /* ================================ hash ================================== */

    /* ================================ list ================================== */
    /**
     * list left push 操作，设置一个 value
     */
    public void lpush(String key, Object value) {
        lpush(key, value, DEFAULT_EXPIRE);
    }

    /**
     * list left push 操作，设置过期时间
     */
    public void lpush(String key, Object value, long expire) {
        listOperations.leftPush(key, value);
        if (expire != NOT_EXPIRE) {
            expire(key, expire);
        }
    }

    /**
     * list left push 操作，设置多个 value
     */
    public void lpush(String key, List<Object> value) {
        lpush(key, value, DEFAULT_EXPIRE);
    }

    /**
     * list left push 操作，设置过期时间
     */
    public void lpush(String key, List<Object> value, long expire) {
        listOperations.leftPushAll(key, value);
        if (expire != NOT_EXPIRE) {
            expire(key, expire);
        }
    }

    /**
     * list right push 操作，设置一个 value
     */
    public void rpush(String key, Object value) {
        rpush(key, value, DEFAULT_EXPIRE);
    }

    /**
     * list right push 操作，设置过期时间
     */
    public void rpush(String key, Object value, long expire) {
        listOperations.rightPush(key, value);
        if (expire != NOT_EXPIRE) {
            expire(key, expire);
        }
    }

    /**
     * list right push 操作，设置多个 value
     */
    public void rpush(String key, List<Object> value) {
        rpush(key, value, DEFAULT_EXPIRE);
    }

    /**
     * list right push 操作，设置过期时间
     */
    public void rpush(String key, List<Object> value, long expire) {
        listOperations.rightPushAll(key, value);
        if (expire != NOT_EXPIRE) {
            expire(key, expire);
        }
    }

    /**
     * list set 操作，根据 index 下标设置 value
     */
    public void lsetIndex(String key, long index, Object value) {
        lsetIndex(key, index, value, DEFAULT_EXPIRE);
    }

    /**
     * list set 操作，设置过期时间
     */
    public void lsetIndex(String key, long index, Object value, long expire) {
        listOperations.set(key, index, value);
        if (expire != NOT_EXPIRE) {
            expire(key, expire);
        }
    }

    /**
     * list range get 操作，获取指定范围的 value
     */
    public List<Object> lrange(String key, long start, long end) {
        return StringUtils.isNotEmpty(key) ? listOperations.range(key, start, end) : null;
    }

    /**
     * list index get 操作，根据指定下标返回 value
     */
    public Object lgetIndex(String key, long index) {
        return StringUtils.isNotEmpty(key) ? listOperations.index(key, index) : null;
    }

    /**
     * list len 操作， 获取 list 长度
     */
    public Long llen(String key) {
        return StringUtils.isNotEmpty(key) ? listOperations.size(key) : null;
    }

    /**
     * list remove 操作，移除 list 中 指定数量的 value
     */
    public Long lremove(String key, long count, Object value) {
        return StringUtils.isNotEmpty(key) ? listOperations.remove(key, count, value) : null;
    }

    /**
     * list trim 操作，截取指定范围的 value，并作为新的 list
     */
    public void ltrim(String key, long start, long end) {
        listOperations.trim(key, start, end);
    }

    /**
     * list left pop 操作，返回头部第一个元素
     */
    public Object lpop(String key) {
        return StringUtils.isNotEmpty(key) ? listOperations.leftPop(key) : null;
    }

    /**
     * list right pop 操作，返回尾部第一个元素
     */
    public Object rpop(String key) {
        return StringUtils.isNotEmpty(key) ? listOperations.rightPop(key) : null;
    }
    /* ================================ list ================================== */

    /* ================================ set ================================== */

    /**
     * set set 操作，设置一个 或 多个 value
     */
    public void sset(String key, Object... value) {
        sset(key, DEFAULT_EXPIRE, value);
    }

    /**
     * set set 操作，设置过期时间
     */
    public void sset(String key, long expire, Object... value) {
        setOperations.add(key, value);
        if (expire != NOT_EXPIRE) {
            expire(key, expire);
        }
    }

    /**
     * set smembers 操作，返回所有 value
     */
    public Set<Object> smembers(String key) {
        return StringUtils.isNotEmpty(key) ? setOperations.members(key) : null;
    }

    /**
     * set sisMember 操作，判断是否存在 某个 value 值
     */
    public Boolean sisMember(String key, Object value) {
        return StringUtils.isNotEmpty(key) ? setOperations.isMember(key, value) : null;
    }

    /**
     * set scard 操作，返回当前 value 个数。
     */
    public Long slen(String key) {
        return StringUtils.isNotEmpty(key) ? setOperations.size(key) : null;
    }

    /**
     * set srem 操作，根据 value 值移除元素
     */
    public Long sremove(String key, Object... value) {
        if (StringUtils.isNotEmpty(key) && value.length > 0) {
            return setOperations.remove(key, value);
        }
        return null;
    }

    /**
     * set spop 操作，随机移除一个元素
     */
    public Object spop(String key) {
        return StringUtils.isNotEmpty(key) ? setOperations.pop(key) : null;
    }

    /**
     * set spop 操作，随机移除 指定个数的元素
     */
    public List<Object> spop(String key, long count) {
        return StringUtils.isNotEmpty(key) ? setOperations.pop(key, count) : null;
    }

    /**
     * set srandmember 操作，随机返回一个 元素（非移除）
     */
    public Object srandomMember(String key) {
        return StringUtils.isNotEmpty(key) ? setOperations.randomMember(key) : null;
    }

    /**
     * set srandmember 操作，随机返回指定个数的 元素（非移除）
     */
    public List<Object> srandomMember(String key, long count) {
        return StringUtils.isNotEmpty(key) ? setOperations.randomMembers(key, count) : null;
    }
    /* ================================ set ================================== */

    /* ================================ zset ================================== */

    /**
     * zset set 操作，设置一个 value
     */
    public void zset(String key, Object value, double score) {
        zset(key, value, score, DEFAULT_EXPIRE);
    }

    /**
     * zset set 操作，设置 过期时间
     */
    public void zset(String key, Object value, double score, long expire) {
        zSetOperations.add(key, value, score);
        if (expire != NOT_EXPIRE) {
            expire(key, expire);
        }
    }

    /**
     * zset set 操作，设置多个 value
     */
    public void zset(String key, Set<ZSetOperations.TypedTuple<Object>> value) {
        zset(key, value, DEFAULT_EXPIRE);
    }

    /**
     * zset set 操作，设置 过期时间
     */
    public void zset(String key, Set<ZSetOperations.TypedTuple<Object>> value, long expire) {
        zSetOperations.add(key, value);
        if (expire != NOT_EXPIRE) {
            expire(key, expire);
        }
    }

    /**
     * zset zrange 操作，返回指定下标范围的 value(升序)
     */
    public Set<Object> zrange(String key, long start, long end) {
        return StringUtils.isNotEmpty(key) ? zSetOperations.range(key, start, end) : null;
    }

    /**
     * zset zrange 操作，返回指定下标范围的 value - score （升序）
     */
    public Set<ZSetOperations.TypedTuple<Object>> zrangeWithScores(String key, long start, long end) {
        return StringUtils.isNotEmpty(key) ? zSetOperations.rangeWithScores(key, start, end) : null;
    }

    /**
     * zset zrange 操作，返回指定 score 范围的 value（升序）
     */
    public Set<Object> zrangeByScore(String key, double min, double max) {
        return StringUtils.isNotEmpty(key) ? zSetOperations.rangeByScore(key, min, max) : null;
    }

    /**
     * zset zrevrange 操作，返回指定下标范围的 value(降序)
     */
    public Set<Object> zreverseRange(String key, long start, long end) {
        return StringUtils.isNotEmpty(key) ? zSetOperations.reverseRange(key, start, end) : null;
    }

    /**
     * zset zrevrange 操作，返回指定 score 范围的 value（降序）
     */
    public Set<Object> zreverseRangeByScore(String key, double min, double max) {
        return StringUtils.isNotEmpty(key) ? zSetOperations.reverseRangeByScore(key, min, max) : null;
    }

    /**
     * zset zrevrange 操作，返回指定下标范围的 value - score （升序）
     */
    public Set<ZSetOperations.TypedTuple<Object>> zreverseRangeWithScores (String key, long start, long end) {
        return StringUtils.isNotEmpty(key) ? zSetOperations.reverseRangeWithScores(key, start, end) : null;
    }

    /**
     * zset zcard 操作，返回 value 个数
     */
    public Long zlen(String key) {
        return StringUtils.isNotEmpty(key) ? zSetOperations.size(key) : null;
    }

    /**
     * zset zcount 操作，返回指定 score 范围内的 value 个数。
     */
    public Long zlenByScore(String key, long min, long max) {
        return StringUtils.isNotEmpty(key) ? zSetOperations.count(key, min, max) : null;
    }

    /**
     * zset zscore 操作，返回指定 value 的 score 值
     */
    public Double zscore(String key, Object value) {
        return StringUtils.isNotEmpty(key) ? zSetOperations.score(key, value) : null;
    }

    /**
     * zset zrem 操作，根据 value 移除一个 或 多个 value
     */
    public Long zremove(String key, Object... value) {
        return StringUtils.isNotEmpty(key) ? zSetOperations.remove(key, value) : null;
    }

    /**
     * zset zremrangebyscore 操作，按照指定 score 范围移除 value
     */
    public Long zremoveByScore(String key, long min, long max) {
        return StringUtils.isNotEmpty(key) ? zSetOperations.removeRangeByScore(key, min, max) : null;
    }

    /**
     * zset zremrangebyrank 操作，按照排序下标范围 移除 value。
     */
    public Long zremoveRange(String key, long start, long end) {
        return StringUtils.isNotEmpty(key) ? zSetOperations.removeRange(key, start, end) : null;
    }

    /**
     * zset zrank 操作，返回升序序列中 value 的排名
     */
    public Long zrank(String key, Object value) {
        return StringUtils.isNotEmpty(key) ? zSetOperations.rank(key, value) : null;
    }

    /**
     * zset zrevrank 操作，返回降序序列中 value 的排名
     */
    public Long zreverseRank(String key, Object value) {
        return StringUtils.isNotEmpty(key) ? zSetOperations.reverseRank(key, value) : null;
    }
    /* ================================ zset ================================== */
}
