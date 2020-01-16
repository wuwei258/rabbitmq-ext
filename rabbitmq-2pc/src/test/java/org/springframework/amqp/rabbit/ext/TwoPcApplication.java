package org.springframework.amqp.rabbit.ext;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.ext.idempotent.redis.EnableRedisIdempotent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude = {DruidDataSourceAutoConfigure.class})
@MapperScan(value = {"org.springframework.amqp.rabbit.ext.dao"})
@EnableTransactionManagement
@EnableScheduling
@EnableRedisIdempotent
public class TwoPcApplication {
    public static void main(String[] args) {
        SpringApplication.run(TwoPcApplication.class, args);
    }
}
