package org.springframework.amqp.rabbit.ext.convert;


import org.springframework.amqp.rabbit.ext.MessageWrap;

/**
 * @ClassName MessageConverterAdapter
 * @Author wuwei
 * @Description
 * @Date 2019/12/20 14:08
 **/
public class MessageConverterAdapter<T> {
    public MessageWrap<T> convert(T message) throws MessageConverterException {
        return getConverter(message).convert(message);
    }

    private MessageConverter<T> defaultConverter = new DefaultMessageConverter();
    private MessageConverter<T> rabbitConverter = new RabbitMessageConverter();

    private MessageConverter<T> getConverter(T message) {
        if (message instanceof org.springframework.amqp.core.Message) {
            return this.rabbitConverter;
        }
        return this.defaultConverter;
    }
}
