package org.springframework.amqp.rabbit.ext.twopc.transaction;

import org.springframework.amqp.rabbit.ext.twopc.transaction.message.TransactionId;


/**
 * @ClassName TransactionMessageSender
 * @Author wuwei
 * @Description NONE
 * @Date 2020/1/15 13:39
 **/
public interface TransactionMessageSender<T extends TransactionId> {
    void sendMessageInTransaction(T message);
}
