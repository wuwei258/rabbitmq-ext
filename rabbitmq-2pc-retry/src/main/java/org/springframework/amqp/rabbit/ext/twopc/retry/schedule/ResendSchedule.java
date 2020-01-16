package org.springframework.amqp.rabbit.ext.twopc.retry.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.ext.twopc.retry.RetryTransactionMessageSender;
import org.springframework.amqp.rabbit.ext.twopc.transaction.message.TransMessageEntity;
import org.springframework.amqp.rabbit.ext.twopc.transaction.service.TransMessageService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName ResendSchedule
 * @Author wuwei
 * @Description 当消息丢入mq时发生错误，此时的消息状态为RE_SEND,本地事务为COMMIT状态，需要再次丢入mq
 * @Date 2020/1/13 16:50
 **/
@Slf4j
@Component
public class ResendSchedule {

    @Resource
    private TransMessageService transMessageService;

    @Resource
    private RetryTransactionMessageSender retryTransactionMessageSender;

    @Scheduled(cron = "*/5 * * * * ?")
    public void schedule() {
        //TODO transMessageService.findResendMessages 需要考虑集群部署时任务如何分配的问题
        //所有RE_SEND状态的消息一定是本地事务提交成功，需要发送至消息队列
        List<TransMessageEntity> transMessageEntities = transMessageService.findResendMessages();
        for (TransMessageEntity transMessageEntity : transMessageEntities) {
            retryTransactionMessageSender.resendTransactionMessage(transMessageEntity);
        }
    }

}
