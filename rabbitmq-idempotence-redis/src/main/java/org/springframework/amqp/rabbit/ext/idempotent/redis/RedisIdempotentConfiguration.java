package org.springframework.amqp.rabbit.ext.idempotent.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 * @ClassName RedisIdempotentConfiguration
 * @Author wuwei
 * @Description NONE
 * @Date 2020/1/15 16:11
 **/
@Configuration
public class RedisIdempotentConfiguration {

    /**
     * 默认key的过期时间
     */
    @Value("${idempotent.check.redis.expire:60}")
    private Integer expire;

    @Bean
    public DefaultRedisScript<String> redisScript() {
        DefaultRedisScript redisScript = new DefaultRedisScript<String>();
        redisScript.setResultType(String.class);
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/idempotent.lua")));
        return redisScript;
    }

    @Bean
    @DependsOn(value = {"stringRedisTemplate", "redisScript"})
    public RedisIdempotentService idempotentService(StringRedisTemplate stringRedisTemplate,
                                                    DefaultRedisScript defaultRedisScript) {
        return new RedisIdempotentService(stringRedisTemplate, defaultRedisScript, expire);
    }
}
