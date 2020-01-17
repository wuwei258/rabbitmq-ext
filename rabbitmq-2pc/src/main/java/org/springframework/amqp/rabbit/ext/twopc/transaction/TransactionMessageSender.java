package org.springframework.amqp.rabbit.ext.twopc.transaction;

import org.springframework.amqp.rabbit.ext.twopc.transaction.message.TransactionId;

/**
 * @ClassName TransactionMessageSender
 * @Author wuwei
 * @Description NONE
 * @Date 2020/1/15 13:39
 **/
public interface TransactionMessageSender<T extends TransactionId> {

    /**
     * 发送事务消息
     * @param message
     */
    void sendMessageInTransaction(T message);
}
