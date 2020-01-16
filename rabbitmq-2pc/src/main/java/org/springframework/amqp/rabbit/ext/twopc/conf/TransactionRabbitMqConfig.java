package org.springframework.amqp.rabbit.ext.twopc.conf;

import org.springframework.amqp.rabbit.ext.twopc.transaction.service.InMemoryTransMessageService;
import org.springframework.amqp.rabbit.ext.twopc.transaction.service.TransMessageService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName TransactionRabbitMqConfig
 * @Author wuwei
 * @Description NONE
 * @Date 2020/1/13 16:20
 **/
@Configuration
public class TransactionRabbitMqConfig {

    @Bean
    @ConditionalOnMissingBean(TransMessageService.class)
    public TransMessageService transMessageService() {
        return new InMemoryTransMessageService();
    }
}
