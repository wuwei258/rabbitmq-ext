package org.springframework.amqp.rabbit.ext.mq;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.ext.QueueUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqConfig {


    @Bean
    public Queue transQueue() {
        return QueueUtils.createDefaultQueue("TRANS_QUEUE");
    }

   /* @Bean
    public Binding binding() {
        return BindingBuilder.bind(transQueue()).to(exchange).with(transQueue().getName()).noargs();
    }*/
}
