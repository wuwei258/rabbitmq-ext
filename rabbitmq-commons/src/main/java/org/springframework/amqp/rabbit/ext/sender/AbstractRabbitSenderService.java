package org.springframework.amqp.rabbit.ext.sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.ext.MessageWrap;
import org.springframework.amqp.rabbit.ext.convert.MessageConverterAdapter;
import org.springframework.amqp.rabbit.ext.convert.MessageConverterException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ClassName AbstractRabbitSenderService
 * @Author wuwei
 * @Description
 * @Date 2019/12/20 14:59
 **/
public abstract class AbstractRabbitSenderService<T> implements MqSenderService<T> {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected MessageConverterAdapter<T> defaultConverterAdapter = new MessageConverterAdapter();

    @Autowired
    protected RabbitTemplate template;

    @Override
    public void send(T data) {
        logger.info("发送消息:{}", data.toString());
        send(convert(data), queue());
    }

    @Override
    public void send(T data, String queue) {
        logger.info("发送消息:{}", data.toString());
        send(convert(data), queue);
    }

    @Override
    public MessageWrap<T> sendConfirm(T param, String correlationDataId) {
        CorrelationData correlationData = new CorrelationData(correlationDataId);
        MessageWrap<T> convert = convert(param);
        send(convert, correlationData, queue());
        return convert;
    }

    @Override
    public MessageWrap<T> sendConfirm(T param, String correlationDataId, String queue) {
        CorrelationData correlationData = new CorrelationData(correlationDataId);
        MessageWrap<T> convert = convert(param);
        send(convert, correlationData, queue);
        return convert;
    }

    private MessageWrap<T> convert(T data) {
        MessageWrap<T> convert;
        try {
            convert = defaultConverterAdapter.convert(data);
            return convert;
        } catch (MessageConverterException e) {
            logger.error("发送消息类型转换异常", e);
            throw new RuntimeException(e);
        }
    }

    protected void send(MessageWrap<?> message, CorrelationData correlationData, String queue) {
        this.template.convertAndSend(queue, message, correlationData);
    }

    protected void send(MessageWrap<?> message, String queue) {
        this.template.convertAndSend(queue, message);
    }

    protected abstract String queue();
}
