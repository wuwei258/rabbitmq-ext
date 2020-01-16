package org.springframework.amqp.rabbit.ext.twopc.transaction;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.springframework.amqp.rabbit.ext.entity.ProduceEntity;
import org.springframework.amqp.rabbit.ext.service.InMemoryProduceService;
import org.springframework.amqp.rabbit.ext.twopc.transaction.message.LocalTransactionState;
import org.springframework.amqp.rabbit.ext.twopc.transaction.message.TransactionId;
import org.springframework.amqp.rabbit.ext.twopc.utils.JacksonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ProduceTransactionListener implements TransactionListener {

    @Resource
    private InMemoryProduceService inMemoryProduceService;

    @Override
    public LocalTransactionState executeLocalTransaction(TransactionId msg, Object... args) {
        ProduceEntity produceEntity =
            ProduceEntity.builder().content(JacksonUtil.obj2String(msg)).id(IdWorker.getId()).build();
        inMemoryProduceService.save(produceEntity);
        return LocalTransactionState.COMMIT_MESSAGE;
    }

    @Override
    public LocalTransactionState checkLocalTransaction(TransactionId msg) {
        return null;
    }
}
