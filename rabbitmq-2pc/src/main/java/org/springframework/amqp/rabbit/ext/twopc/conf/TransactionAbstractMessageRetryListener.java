package org.springframework.amqp.rabbit.ext.twopc.conf;

import org.springframework.amqp.rabbit.ext.conf.AbstractMessageRetryListener;
import org.springframework.amqp.rabbit.ext.twopc.transaction.service.TransMessageService;

/**
 * @ClassName TransactionMessageRetryListener
 * @Author wuwei
 * @Description NONE
 * @Date 2020/1/19 15:36
 **/
public class TransactionAbstractMessageRetryListener extends AbstractMessageRetryListener {

    private TransMessageService transMessageService;

    public TransactionAbstractMessageRetryListener(TransMessageService transMessageService) {
        this.transMessageService = transMessageService;
    }

    @Override
    protected void setRetry(String correlationId) {
        transMessageService.customerRetryMessage(correlationId);
    }

    @Override
    protected void resetRetry(String correlationId) {

    }

    @Override
    protected void addRetryTimes(String correlationId) {

    }

}
