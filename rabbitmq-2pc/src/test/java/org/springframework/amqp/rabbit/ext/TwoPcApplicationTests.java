package org.springframework.amqp.rabbit.ext;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.ext.entity.SendMessage;
import org.springframework.amqp.rabbit.ext.mq.ProducerSendService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest(classes = TwoPcApplication.class)
@RunWith(SpringRunner.class)
class TwoPcApplicationTests {
    @Resource
    private ProducerSendService producerSendService;

    @Test
    void contextLoads() {
    }

    @Test
    void mqTest() throws InterruptedException {
        SendMessage sendMessage = SendMessage.builder().id(IdWorker.getId()).content("i am test message").build();
        producerSendService.sendMessageInTransaction(sendMessage);
        Thread.sleep(100000L);
    }

}
