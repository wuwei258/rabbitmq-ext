package org.springframework.amqp.rabbit.ext.twopc.transaction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.ext.sender.AbstractRabbitSenderService;
import org.springframework.amqp.rabbit.ext.twopc.transaction.message.LocalTransactionState;
import org.springframework.amqp.rabbit.ext.twopc.transaction.message.TransMessageEntity;
import org.springframework.amqp.rabbit.ext.twopc.transaction.message.TransactionId;
import org.springframework.amqp.rabbit.ext.twopc.transaction.service.TransMessageService;
import org.springframework.amqp.rabbit.ext.twopc.utils.JacksonUtil;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


/**
 * @ClassName AbstractTransactionMessageSender
 * @Author wuwei
 * @Description NONE
 * @Date 2020/1/15 13:39
 **/
@Slf4j
public abstract class AbstractTransactionMessageSender<T extends TransactionId> extends AbstractRabbitSenderService<T>
    implements TransactionMessageSender<T> {

    private static final Map<String, Class> CLASS_MAP = new HashMap<>();

    @Resource
    private TransMessageService transMessageService;

    @Override
    public void sendMessageInTransaction(T message) {
        TransactionListener transactionListener = transactionListener();
        checkTransactionListener(transactionListener);
        checkMessage(message);
        // 1.prepare
        TransMessageEntity transMessageEntity = TransMessageEntity.builder().transactionId(message.transactionId())
            .messageContent(JacksonUtil.obj2String(message)).queueName(queue()).clazz(message.getClass().getName())
            .build();
        transMessageService.prepareMessage(transMessageEntity);
        // 2.执行本地事物
        LocalTransactionState localTransactionState = transactionListener.executeLocalTransaction(message, null);
        // 3.根据本地事务状态判断是否需要commit
        afterLocalTransaction(localTransactionState, message, transMessageEntity);
    }

    /**
     * 根据本地事务处理情况确认消息执行逻辑
     *
     * @param localTransactionState
     * @param message
     * @param transMessageEntity
     */
    public void afterLocalTransaction(LocalTransactionState localTransactionState, T message,
        TransMessageEntity transMessageEntity) {
        transMessageEntity.setLocalTransactionState(localTransactionState);
        switch (localTransactionState) {
            //当本地事务执行完成后，需将消息发送至队列中
            //如果消息成功发送后，消息状态改为已发送
            //如果消息发送失败，如网络异常，则消息状态改为重新发送，待定时任务重新将消息丢入队列
            //最终的消息ACK由MessageConfirmCallback来执行
            case COMMIT_MESSAGE:
                try {
                    sendToMq(message);
                    transMessageService.commitMessage(transMessageEntity.getTransactionId());
                } catch (Exception e) {
                    log.error("发送消息至MQ失败", e);
                    transMessageService.resendMessage(transMessageEntity.getTransactionId());
                }
                break;
            case ROLLBACK_MESSAGE:
                transMessageService.rollbackMessage(message.transactionId());
                break;
        }
    }

    /**
     * 使用确认模式发送至rabbitmq 当消息confirm失败后会有重试机制将消息再次发送
     *
     * @param message
     */
    private void sendToMq(T message) {
        try {
            sendConfirm(message, message.transactionId());
        } catch (Exception e) {
            logger.error("发送消息至mq失败:{}", e);
            throw new RuntimeException(e);
        }
    }

    private void checkMessage(T message) {
        if (null == message) {
            throw new RuntimeException("message is null");
        }
        if (!(message instanceof TransactionId)) {
            throw new RuntimeException("transaction message must implements TransactionId");
        }
    }

    private void checkTransactionListener(TransactionListener transactionListener) {
        if (null == transactionListener) {
            throw new RuntimeException("TransactionListener is null");
        }
    }

    @Override
    public abstract String queue();

    public abstract TransactionListener transactionListener();
}
