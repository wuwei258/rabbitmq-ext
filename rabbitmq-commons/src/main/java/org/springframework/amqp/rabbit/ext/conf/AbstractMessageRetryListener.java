package org.springframework.amqp.rabbit.ext.conf;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;

import static org.springframework.amqp.rabbit.ext.MqConstants.CORRELATION_ID;

/**
 * @ClassName TransactionMessageRetryListener
 * @Author wuwei
 * @Description NONE
 * @Date 2020/1/19 11:07
 **/
@Slf4j
public abstract class AbstractMessageRetryListener implements RetryListener {

    @Override
    public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
        String correlationId = correlationId(callback);
        if (!StringUtils.isEmpty(correlationId)) {
            log.info("--------->retry open{}", correlationId);
            addRetryTimes(correlationId);
        }
        return true;
    }

    @Override
    public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback,
        Throwable throwable) {
        String correlationId = correlationId(callback);
        if (!StringUtils.isEmpty(correlationId)) {
            log.info("--------->retry close{}", correlationId);
            resetRetry(correlationId);
        }
    }

    @Override
    public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback,
        Throwable throwable) {
        String correlationId = correlationId(callback);
        log.info("TransactionMessageRetryListener onError{}", correlationId);
        if (!StringUtils.isEmpty(correlationId)) {
            setRetry(correlationId);
        }
    }

    protected String correlationId(RetryCallback callback) {
        Field invocation = ReflectionUtils.findField(callback.getClass(), "invocation");
        if (null != invocation) {
            ReflectionUtils.makeAccessible(invocation);
            Object field = ReflectionUtils.getField(invocation, callback);
            if (null != field) {
                MethodInvocation methodInvocation = (MethodInvocation)field;
                Object[] arguments = methodInvocation.getArguments();
                if (arguments[1] instanceof Message) {
                    Message message = (Message)arguments[1];
                    MessageProperties messageProperties = message.getMessageProperties();
                    String correlationId = messageProperties.getHeader(CORRELATION_ID);
                    return correlationId;
                }
            }
        }
        return null;
    }

    /**
     * 设置当前消息为重试消息
     *
     * @param correlationId
     */
    protected abstract void setRetry(String correlationId);

    /**
     * 清空重试信息
     *
     * @param correlationId
     */
    protected abstract void resetRetry(String correlationId);

    /**
     * 增加重试次数
     *
     * @param correlationId
     */
    protected abstract void addRetryTimes(String correlationId);

}
