package org.springframework.amqp.rabbit.ext.twopc.transaction.service;

import org.springframework.amqp.rabbit.ext.twopc.transaction.message.TransMessageEntity;

public abstract class AbstractTransMessageServiceImpl implements TransMessageService {

    @Override
    public void prepareMessage(TransMessageEntity entity) {
        prepareMessageEntity(entity);
    }

    protected abstract void prepareMessageEntity(TransMessageEntity entity);

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
