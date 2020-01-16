package org.springframework.amqp.rabbit.ext.twopc.retry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.ext.twopc.transaction.AbstractTransactionMessageSender;
import org.springframework.amqp.rabbit.ext.twopc.transaction.TransactionListener;
import org.springframework.amqp.rabbit.ext.twopc.transaction.message.LocalTransactionState;
import org.springframework.amqp.rabbit.ext.twopc.transaction.message.TransMessageEntity;
import org.springframework.amqp.rabbit.ext.twopc.transaction.message.TransactionId;
import org.springframework.amqp.rabbit.ext.twopc.utils.JacksonUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName TransactionMessageSendService
 * @Author wuwei
 * @Description NONE
 * @Date 2020/1/15 13:39
 **/
@Slf4j
@Service
public class RetryTransactionMessageSender extends AbstractTransactionMessageSender {

    private static final Map<String, Class<? extends TransactionId>> CLASS_MAP = new HashMap<>();
    private String queue;

    public void resendTransactionMessage(TransMessageEntity transMessageEntity) {
        String queueName = transMessageEntity.getQueueName();
        setQueue(queueName);
        try {
            Class<? extends TransactionId> forName = findClass(transMessageEntity.getClazz());
            Object message = JacksonUtil.string2Obj(transMessageEntity.getMessageContent(), forName);
            afterLocalTransaction(LocalTransactionState.COMMIT_MESSAGE, (TransactionId)message, transMessageEntity);
        } catch (Exception e) {
            log.error("获取消息实体类型失败", e);
        }
    }

    private Class findClass(String className) {
        Class clazz = CLASS_MAP.get(className);
        if (null == clazz) {
            try {
                clazz = Class.forName(className);
                CLASS_MAP.put(className, clazz);
            } catch (ClassNotFoundException e) {
                log.error("未获取到class{}", className);
                throw new RuntimeException(e);
            }
        }
        return clazz;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    @Override
    public String queue() {
        return queue;
    }

    @Override
    public TransactionListener transactionListener() {
        return null;
    }
}
