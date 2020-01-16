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
 * @ClassName LocalTransactionCheckSchedule
 * @Author wuwei
 * @Description 当mq confirmCallback时发生网络问题，消息可能会产生PREPARED状态本地事务为COMMIT状态
 * 这种状态的消息需要再次发送
 * @Date 2020/1/15 11:51
 **/
@Slf4j
@Component
public class LocalTransactionCheckSchedule {

    @Resource
    private TransMessageService transMessageService;
    @Resource
    private RetryTransactionMessageSender retryTransactionMessageSender;

    @Scheduled(cron = "*/5 * * * * ?")
    public void schedule() {
        List<TransMessageEntity> transMessageEntities = transMessageService.findLocalDoneNotCommit();
        for (TransMessageEntity transMessageEntity : transMessageEntities) {
            retryTransactionMessageSender.resendTransactionMessage(transMessageEntity);
        }
    }

}
