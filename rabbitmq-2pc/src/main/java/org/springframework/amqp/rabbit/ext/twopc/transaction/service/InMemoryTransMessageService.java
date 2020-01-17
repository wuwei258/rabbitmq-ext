package org.springframework.amqp.rabbit.ext.twopc.transaction.service;

import org.springframework.amqp.rabbit.ext.twopc.transaction.message.LocalTransactionState;
import org.springframework.amqp.rabbit.ext.twopc.transaction.message.MessageState;
import org.springframework.amqp.rabbit.ext.twopc.transaction.message.TransMessageEntity;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @ClassName InMemoryTransMessageService
 * @Author wuwei
 * @Description NONE
 * @Date 2020/1/13 16:16
 **/
public class InMemoryTransMessageService extends AbstractTransMessageServiceImpl {

    private static final Map<String, TransMessageEntity> DATA_BASE = new ConcurrentHashMap<>();

    @Override
    protected void prepareMessageEntity(TransMessageEntity entity) {
        entity.setMessageState(MessageState.PREPARE);
        DATA_BASE.put(entity.getTransactionId(), entity);
    }

    @Override
    protected void sendMessageEntity(String transactionId) {
        TransMessageEntity transMessageEntity = DATA_BASE.get(transactionId);
        transMessageEntity.setMessageState(MessageState.SEND);
        DATA_BASE.put(transactionId, transMessageEntity);
    }

    @Override
    protected void commitMessageEntity(String transactionId) {
        TransMessageEntity transMessageEntity = DATA_BASE.get(transactionId);
        transMessageEntity.setMessageState(MessageState.COMMIT);
        DATA_BASE.put(transactionId, transMessageEntity);
    }

    @Override
    protected void rollbackMessageEntity(String transactionId) {
        DATA_BASE.remove(transactionId);
    }

    @Override
    protected void resendMessageEntity(String transactionId) {
        TransMessageEntity transMessageEntity = DATA_BASE.get(transactionId);
        transMessageEntity.setMessageState(MessageState.RE_SEND);
        DATA_BASE.put(transactionId, transMessageEntity);
    }

    @Override
    public List<TransMessageEntity> findResendMessages() {
        Collection<TransMessageEntity> values = DATA_BASE.values();
        return values.stream()
            .filter(transMessageEntity -> transMessageEntity.getMessageState() == MessageState.RE_SEND)
            .collect(Collectors.toList());
    }

    @Override
    public List<TransMessageEntity> findLocalDoneNotCommit() {
        Collection<TransMessageEntity> values = DATA_BASE.values();
        return values.stream().filter(transMessageEntity -> transMessageEntity.getMessageState() == MessageState.PREPARE
            && transMessageEntity.getLocalTransactionState() == LocalTransactionState.COMMIT_MESSAGE)
            .collect(Collectors.toList());
    }
}
