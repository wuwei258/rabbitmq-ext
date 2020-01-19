package org.springframework.amqp.rabbit.ext.twopc.conf;

import org.springframework.amqp.rabbit.ext.conf.ShouldIdempotentCheckService;
import org.springframework.amqp.rabbit.ext.twopc.transaction.service.TransMessageService;

/**
 * @ClassName TransactionMessageIdempotentCheckService
 * @Author wuwei
 * @Description NONE
 * @Date 2020/1/19 16:07
 **/
public class TransactionMessageIdempotentCheckService implements ShouldIdempotentCheckService {
    private TransMessageService transMessageService;

    public TransactionMessageIdempotentCheckService(TransMessageService transMessageService) {
        this.transMessageService = transMessageService;
    }

    @Override
    public Boolean shouldCheck(String id) {
        return !transMessageService.shouldCheckMessage(id);
    }
}
