package com.lyh.admin_template.back;

import com.lyh.admin_template.back.common.utils.RedisUtil;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.util.CollectionUtils;

import java.util.*;

@SpringBootTest
public class TestRedis {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 测试 Redis common 操作
     */
    @Test
    void testCommon() {
        redisUtil.set("hello", "10", 100);
        System.out.println("所有的 key: " + redisUtil.keys("*"));
        System.out.println("key 中是否存在 hello: " + redisUtil.hasKey("hello"));
        System.out.println("获取 hello 的过期时间: " + redisUtil.getExpire("hello"));
        redisUtil.del("hello");
        System.out.println("获取 hello 的过期时间: " + redisUtil.getExpire("hello"));
    }

    /**
     * 测试 Redis string 操作
     */
    @Test
    void testString() {
        System.out.println("所有的 key: " + redisUtil.keys("*"));
        redisUtil.set("hello", "10", 100);
        System.out.println(redisUtil.get("hello"));
        System.out.println(redisUtil.incr("hello",100));
        System.out.println(redisUtil.get("hello"));
    }

    /**
     * 测试 Redis hash 操作
     */
    @Test
    void testHash() {
        List<Student> lists = new ArrayList<>();
        lists.add(new Student("tom"));
        lists.add(new Student("jarry"));
        Teacher teacher = new Teacher();
        teacher.setName("teacher");
        teacher.setStudents(lists);

        Map<String, Object> map = new HashMap<>();
        map.put("tom", 12);
        map.put("jarry", 24);

        redisUtil.hset("hash", "hello", teacher);
        redisUtil.hmset("hash", map, 100);

        System.out.println(redisUtil.hkeys("hash"));
        System.out.println(redisUtil.hHasKey("hash", "hello"));
        System.out.println(redisUtil.hget("hash", "hello"));
        System.out.println(redisUtil.hgetAll("hash"));
        System.out.println(redisUtil.hmget("hash"));
        System.out.println(redisUtil.getExpire("hash"));

        redisUtil.del("hash", "hello", "hello2");
        System.out.println(redisUtil.hkeys("hash"));
    }

    /**
     * 测试 Redis list 操作
     */
    @Test
    void testList() {
        redisUtil.lpush("list", 1);
        redisUtil.rpush("list", 2);
        redisUtil.lpush("list", CollectionUtils.arrayToList(new int[]{3, 4, 5}));
        redisUtil.rpush("list", CollectionUtils.arrayToList(new int[]{6, 7, 8}));

        System.out.println(redisUtil.llen("list"));
        System.out.println(redisUtil.lrange("list", 0, -1));

        redisUtil.lsetIndex("list", 2, 10);
        System.out.println(redisUtil.lgetIndex("list", 2));

        System.out.println(redisUtil.lrange("list", 0, -1));
        System.out.println(redisUtil.lremove("list", 10, 2));
        System.out.println(redisUtil.lrange("list", 0, -1));

        redisUtil.ltrim("list", 0, 3);
        System.out.println(redisUtil.lrange("list", 0, -1));

        System.out.println(redisUtil.lpop("list"));
        System.out.println(redisUtil.rpop("list"));
        System.out.println(redisUtil.lrange("list", 0, -1));

        redisUtil.del("list");
    }

    /**
     * 测试 Redis set 操作
     */
    @Test
    void testSet() {
        redisUtil.sset("set", "1");
        redisUtil.sset("set", "1", "2", "3");
        System.out.println(redisUtil.smembers("set"));
        System.out.println(redisUtil.sisMember("set", "2"));
        System.out.println(redisUtil.slen("set"));

        System.out.println(redisUtil.srandomMember("set", 10));
        System.out.println(redisUtil.smembers("set"));

        System.out.println(redisUtil.spop("set"));
        System.out.println(redisUtil.smembers("set"));

        redisUtil.del("set");
    }

    /**
     * 测试 Redis zset 操作
     */
    @Test
    void testZset() {
        redisUtil.zset("zset", "20", 1);
        redisUtil.zset("zset", "10", 1);
        redisUtil.zset("zset", "30", 2);
        System.out.println(redisUtil.zrange("zset", 0, -1));
        System.out.println(redisUtil.zrangeByScore("zset", 0, 1));

        System.out.println(redisUtil.zreverseRange("zset", 0, -1));
        System.out.println(redisUtil.zreverseRangeByScore("zset", 0, 1));

        System.out.println(redisUtil.zlen("zset"));
        System.out.println(redisUtil.zlenByScore("zset", 0, 1));

        System.out.println(redisUtil.zscore("zset", "20"));

//        System.out.println(redisUtil.zremove("zset", "20", "30", "40"));
//        System.out.println(redisUtil.zremoveRange("zset", 1, 4));
        System.out.println(redisUtil.zremoveByScore("zset", 2, 4));
        System.out.println(redisUtil.zrange("zset", 0, -1));

        redisUtil.zset("zset", new Student("tom"), 10);
        redisUtil.zset("zset", new Student("jarry"), 20);
        System.out.println(redisUtil.zrange("zset", 0, -1));
        System.out.println(redisUtil.zrank("zset", new Student("tom")));
        System.out.println(redisUtil.zreverseRange("zset", 0, -1));
        System.out.println(redisUtil.zreverseRank("zset", new Student("tom")));

        Set<ZSetOperations.TypedTuple<Object>> set = new HashSet<>();
        set.add(new DefaultTypedTuple<Object>("20", 1.0));
        set.add(new DefaultTypedTuple<Object>("10", 1.0));
        set.add(new DefaultTypedTuple<Object>("30", 2.0));
        redisUtil.zset("zset", set);
        redisUtil.zrangeWithScores("zset", 0, -1).forEach((item) -> {
            System.out.println(item.getValue() + "===" + item.getScore());
        });

        redisUtil.zreverseRangeWithScores("zset", 0, -1).forEach((item) -> {
            System.out.println(item.getValue() + "===" + item.getScore());
        });

        redisUtil.del("zset");
    }
}
@Data
class Teacher {
    private List<Student> students;
    private String name;
}

@Data
class Student {
    private String name;
    Student() {

    }
    Student(String name) {
        this.name = name;
    }
}
