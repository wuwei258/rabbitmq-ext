package org.springframework.amqp.rabbit.ext;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.ext.entity.SendMessage;
import org.springframework.stereotype.Component;

/**
 * @ClassName TestRabbitMqListener
 * @Author wuwei
 * @Description NONE
 * @Date 2020/1/13 16:34
 **/
@Component
public class TestRabbitMqListener {

    @RabbitListener(queues = {"TRANS_QUEUE"}, containerFactory = ListenerSelector.multiThreadAndManualAcknowledge)
    public void listener(MessageWrap<SendMessage> messageWrap) {

        System.out.println(messageWrap);
        throw new RuntimeException("listener抛出异常");
    }
}
