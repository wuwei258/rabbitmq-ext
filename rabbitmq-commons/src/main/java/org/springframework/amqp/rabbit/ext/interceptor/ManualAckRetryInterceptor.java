package org.springframework.amqp.rabbit.ext.interceptor;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.ext.MqUtils;
import org.springframework.amqp.rabbit.support.DefaultMessagePropertiesConverter;
import org.springframework.amqp.rabbit.support.MessagePropertiesConverter;

/**
 * @ClassName ManualAckInterceptor
 * @Author wuwei
 * @Description 手动ack拦截器
 * @Date 2020/1/14 15:47
 **/
@Slf4j
public class ManualAckRetryInterceptor implements MethodInterceptor {

    private static final Integer RETRY_TIMES = 3;

    private static final String RETRY_TIMES_HEADER = "retry_times_header";
    private boolean manualAck;

    private MessagePropertiesConverter messagePropertiesConverter = new DefaultMessagePropertiesConverter();

    public ManualAckRetryInterceptor(boolean manualAck) {
        this.manualAck = manualAck;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        if (manualAck) {
            Object[] arguments = methodInvocation.getArguments();
            Channel channel = (Channel) arguments[0];
            Message message = (Message) arguments[1];
            MessageProperties messageProperties = message.getMessageProperties();
            Integer header = messageProperties.getHeader(RETRY_TIMES_HEADER);
            if (null == header) {
                header = Integer.valueOf(1);
                messageProperties.setHeader(RETRY_TIMES_HEADER, header);
            }
            Object result = null;
            try {
                result = methodInvocation.proceed();
            } catch (Throwable throwable) {
                if (header < RETRY_TIMES) {
                    messageProperties.setHeader(RETRY_TIMES_HEADER, header + 1);
                    AMQP.BasicProperties basicProperties =
                            messagePropertiesConverter.fromMessageProperties(messageProperties, "UTF-8");
                    //将消息重新推入队列中
                    channel.basicPublish(messageProperties.getReceivedExchange(),
                            messageProperties.getReceivedRoutingKey(), basicProperties, message.getBody());
                    MqUtils.ack(message,channel);
                } else {
                    //超过最大重试次数后直接ack掉 之后需要加入异常处理逻辑
                    log.info("消息{}重试{}次后继续抛出异常,将其ACK掉不再处理", message, RETRY_TIMES);
                    MqUtils.ack(message,channel);
                    //TODO 超过最大重试次数后的处理逻辑例如丢入一个死信队列
                }
                throw new RuntimeException(throwable);
            }
            MqUtils.ack(message,channel);
            return result;
        } else {
            return methodInvocation.proceed();
        }

    }
}
