package org.springframework.amqp.rabbit.ext.mq;

import org.springframework.amqp.rabbit.ext.twopc.transaction.AbstractTransactionMessageSender;
import org.springframework.amqp.rabbit.ext.twopc.transaction.TransactionListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ProducerSendService extends AbstractTransactionMessageSender {

    @Resource(name = "produceTransactionListener")
    private TransactionListener transactionListener;

    @Override
    public String queue() {
        return "TRANS_QUEUE";
    }

    @Override
    public TransactionListener transactionListener() {
        return transactionListener;
    }
}
