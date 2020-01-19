package org.springframework.amqp.rabbit.ext.interceptor;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.ext.MqUtils;
import org.springframework.amqp.rabbit.ext.conf.ShouldIdempotentCheckService;
import org.springframework.amqp.rabbit.ext.idempotent.IdempotentService;

import static org.springframework.amqp.rabbit.ext.MqConstants.CORRELATION_ID;
import static org.springframework.amqp.rabbit.ext.MqConstants.RETRY_HEADER;

/**
 * @ClassName IdempotentInterceptor
 * @Author wuwei
 * @Description 幂等校验拦截器
 * @Date 2020/1/16 11:56
 **/
@Slf4j
public class IdempotentInterceptor implements MethodInterceptor {

    private IdempotentService idempotentService;

    private ShouldIdempotentCheckService shouldIdempotentCheckService;

    public IdempotentInterceptor(IdempotentService idempotentService,
        ShouldIdempotentCheckService shouldIdempotentCheckService) {
        this.idempotentService = idempotentService;
        this.shouldIdempotentCheckService = shouldIdempotentCheckService;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        if (null != idempotentService) {
            Object[] arguments = methodInvocation.getArguments();
            Channel channel = (Channel)arguments[0];
            Message message = (Message)arguments[1];
            MessageProperties messageProperties = message.getMessageProperties();
            String correlationId = messageProperties.getHeader(CORRELATION_ID);
            //重试的消息不做幂等判断
            if (shouldCheck(messageProperties)) {
                if (!idempotentService.idempotentCheck(messageProperties.getConsumerQueue(), correlationId)) {
                    log.info("幂等校验失败,队列{},key{}", messageProperties.getConsumerQueue(), correlationId);
                    MqUtils.ack(message, channel);
                    return null;
                }
            }
            return methodInvocation.proceed();
        } else {
            return methodInvocation.proceed();
        }
    }

    private boolean shouldCheck(MessageProperties messageProperties) {
        String correlationId = messageProperties.getHeader(CORRELATION_ID);
        Integer retry = messageProperties.getHeader(RETRY_HEADER);
        return null != retry || shouldIdempotentCheckService.shouldCheck(correlationId);
    }
}
