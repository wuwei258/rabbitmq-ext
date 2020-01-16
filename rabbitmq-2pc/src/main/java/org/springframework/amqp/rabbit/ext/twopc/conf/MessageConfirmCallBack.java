package org.springframework.amqp.rabbit.ext.twopc.conf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.ext.twopc.transaction.service.TransMessageService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName MessageConfirmCallBack
 * @Author wuwei
 * @Description NONE
 * @Date 2020/1/8 9:10
 **/
@Slf4j
@Component
public class MessageConfirmCallBack implements RabbitTemplate.ConfirmCallback {

    @Resource
    private TransMessageService transMessageService;

    public MessageConfirmCallBack(TransMessageService transMessageService) {
        this.transMessageService = transMessageService;
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info("confirm确认回调......");
        if (null == correlationData) {
            return;
        }
        String id = correlationData.getId();
        if (ack) {
            transMessageService.commitMessage(id);
        } else {
            log.info("指令{}未成功发送至mq", id);
            transMessageService.resendMessage(id);
        }
    }
}
