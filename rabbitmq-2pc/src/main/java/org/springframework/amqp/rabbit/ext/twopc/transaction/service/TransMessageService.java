package org.springframework.amqp.rabbit.ext.twopc.transaction.service;

import org.springframework.amqp.rabbit.ext.twopc.transaction.message.TransMessageEntity;

import java.util.List;

public interface TransMessageService {

    /**
     * prepare事务消息
     *
     * @param entity
     */
    void prepareMessage(TransMessageEntity entity);

    /**
     * 事务消息改为commit状态，已发送至交换机且交换机已确认接收
     *
     * @param transactionId
     */
    void commitMessage(String transactionId);

    /**
     * 回滚事务消息，不发送至mq
     *
     * @param transactionId
     */
    void rollbackMessage(String transactionId);

    /**
     * 事务消息改为需要重新发送状态
     *
     * @param transactionId
     */
    void resendMessage(String transactionId);

    /**
     * 查询需重新发送的事务消息
     *
     * @return
     */
    List<TransMessageEntity> findResendMessages();

    /**
     * 查询本地事务已提交但是状态还是为prepare的事务消息
     *
     * @return
     */
    List<TransMessageEntity> findLocalDoneNotCommit();

}
