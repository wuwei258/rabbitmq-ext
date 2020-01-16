package org.springframework.amqp.rabbit.ext.idempotent.redis;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @className EnableRedisIdempotent
 * @author wuwei
 * @description
 * @date 2020/1/15 16:11
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(RedisIdempotentConfiguration.class)
@Documented
public @interface EnableRedisIdempotent {
}
