package org.springframework.amqp.rabbit.ext.twopc.retry;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;


/**
 * @ClassName TwoPcApplication
 * @Author wuwei
 * @Description NONE
 * @Date 2020/1/15 13:39
 **/
@SpringBootApplication(exclude = {DruidDataSourceAutoConfigure.class})
@EnableScheduling
public class TwoPcApplication {

    public static void main(String[] args) {
        SpringApplication.run(TwoPcApplication.class, args);
    }

}
