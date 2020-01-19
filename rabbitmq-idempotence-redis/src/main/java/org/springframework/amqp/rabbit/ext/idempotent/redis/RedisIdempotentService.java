package org.springframework.amqp.rabbit.ext.idempotent.redis;

import org.springframework.amqp.rabbit.ext.idempotent.IdempotentService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName IdempotentService
 * @Author wuwei
 * @Description NONE
 * @Date 2020/1/15 16:06
 **/
public class RedisIdempotentService implements IdempotentService {

    private static final String KEY_FIX = ":";

    private StringRedisTemplate stringRedisTemplate;

    private DefaultRedisScript<String> redisScript;

    private Integer expire;

    public RedisIdempotentService(StringRedisTemplate stringRedisTemplate, DefaultRedisScript<String> redisScript,
        Integer expire) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisScript = redisScript;
        this.expire = expire;
    }

    @Override
    public boolean idempotentCheck(String business, String key) {
        List<String> params = new ArrayList<>();
        params.add(business + KEY_FIX + key);
        params.add(key);
        String execute = stringRedisTemplate.execute(redisScript, params, expire.toString());
        return Boolean.valueOf(execute);
    }

}
