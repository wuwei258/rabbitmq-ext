package org.springframework.amqp.rabbit.ext.twopc.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.ext.twopc.transaction.service.TransMessageService;

import javax.annotation.Resource;

import static org.springframework.amqp.rabbit.ext.MqConstants.CORRELATION_ID;

/**
 * @ClassName TransactionInterceptor
 * @Author wuwei
 * @Description NONE
 * @Date 2020/1/19 9:53
 **/
@Slf4j
public class TransactionMessageInterceptor implements MethodInterceptor {

    @Resource
    private TransMessageService transMessageService;

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Object[] arguments = methodInvocation.getArguments();
        Message message = (Message)arguments[1];
        MessageProperties messageProperties = message.getMessageProperties();
        String correlationId = messageProperties.getHeader(CORRELATION_ID);
        Object object = null;
        try {
            object = methodInvocation.proceed();
        } catch (Throwable throwable) {
            throw throwable;
        }
        transMessageService.doneMessage(correlationId);
        return object;
    }
}
