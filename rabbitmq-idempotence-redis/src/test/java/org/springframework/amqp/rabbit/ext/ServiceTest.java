package org.springframework.amqp.rabbit.ext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.ext.idempotent.redis.RedisIdempotentService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @ClassName ServiceTest
 * @Author wuwei
 * @Description NONE
 * @Date 2020/1/15 16:23
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class ServiceTest {

    @Resource
    private RedisIdempotentService redisIdempotenceService;

    @Test
    public void test() {
        System.out.println(redisIdempotenceService.idempotentCheck("TEST", "123"));
        System.out.println(redisIdempotenceService.idempotentCheck("TEST", "123"));
        System.out.println(redisIdempotenceService.idempotentCheck("TEST", "456"));
    }
}
