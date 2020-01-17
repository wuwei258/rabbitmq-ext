package org.springframework.amqp.rabbit.ext.twopc.transaction.service;

import org.springframework.amqp.rabbit.ext.twopc.transaction.message.TransMessageEntity;

/**
 * @className AbstractTransMessageServiceImpl
 * @author wuwei
 * @description
 * @date  2020/1/17 15:55
 **/
public abstract class AbstractTransMessageServiceImpl implements TransMessageService {

    @Override
    public void prepareMessage(TransMessageEntity entity) {
        prepareMessageEntity(entity);
    }

    protected abstract void prepareMessageEntity(TransMessageEntity entity);

    @Override
    public void sendMessage(String transactionId) {
        sendMessageEntity(transactionId);
    }

    protected abstract void sendMessageEntity(String transactionId);

    @Override
    public void commitMessage(String transactionId) {
        commitMessageEntity(transactionId);
    }

    protected abstract void commitMessageEntity(String transactionId);

    @Override
    public void rollbackMessage(String transactionId) {
        rollbackMessageEntity(transactionId);
    }

    protected abstract void rollbackMessageEntity(String transactionId);

    @Override
    public void resendMessage(String transactionId) {
        resendMessageEntity(transactionId);
    }

    protected abstract void resendMessageEntity(String transactionId);

}
