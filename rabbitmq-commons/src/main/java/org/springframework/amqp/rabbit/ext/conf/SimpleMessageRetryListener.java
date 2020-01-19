package org.springframework.amqp.rabbit.ext.conf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.ext.MqConstants;

/**
 * @ClassName SimpleMessageRetryListener
 * @Author wuwei
 * @Description NONE
 * @Date 2020/1/19 15:54
 **/
@Slf4j
public class SimpleMessageRetryListener extends AbstractMessageRetryListener {

    @Override
    protected void setRetry(String correlationId) {
        SpringRetryMessageCorrelationHolder.set(correlationId);
    }

    @Override
    protected void resetRetry(String correlationId) {
        Integer retryTimes = SpringRetryMessageCorrelationHolder.getRetryTimes(correlationId);
        if (retryTimes > MqConstants.RETRY_TIMES) {
            SpringRetryMessageCorrelationHolder.clean(correlationId);
        }
    }

    @Override
    protected void addRetryTimes(String correlationId) {
        SpringRetryMessageCorrelationHolder.addRetryTimes(correlationId);
    }
}
