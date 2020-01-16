package org.springframework.amqp.rabbit.ext.interceptor;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.ext.idempotent.IdempotentService;

/**
 * @ClassName IdempotentInterceptor
 * @Author wuwei
 * @Description 幂等校验拦截器
 * @Date 2020/1/16 11:56
 **/
@Slf4j
public class IdempotentInterceptor implements MethodInterceptor {

    private static final String CORRELATION_ID = "spring_returned_message_correlation";
    private static final String RETRY_HEADER = "retry_times_header";
    private IdempotentService idempotentService;

    public IdempotentInterceptor(IdempotentService idempotentService) {
        this.idempotentService = idempotentService;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        if (null != idempotentService) {
            Object[] arguments = methodInvocation.getArguments();
            Channel channel = (Channel)arguments[0];
            Message message = (Message)arguments[1];
            MessageProperties messageProperties = message.getMessageProperties();
            String correlationId = messageProperties.getHeader(CORRELATION_ID);
            Integer retry = messageProperties.getHeader(RETRY_HEADER);
            //重试的消息不做幂等判断
            if (null == retry) {
                if (!idempotentService.idempotentCheck(messageProperties.getConsumerQueue(), correlationId)) {
                    log.info("幂等校验失败,队列{},key{}", messageProperties.getConsumerQueue(), correlationId);
                    channel.basicAck(messageProperties.getDeliveryTag(), false);
                    return null;
                }
            }
            return methodInvocation.proceed();
        } else {
            return methodInvocation.proceed();
        }
    }
}
