package org.springframework.amqp.rabbit.ext.convert;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.ext.MessageWrap;
import org.springframework.amqp.support.converter.SimpleMessageConverter;

/**
 * @ClassName RabbitMessageConverter
 * @Author wuwei
 * @Description
 * @Date 2019/12/20 14:27
 **/
public class RabbitMessageConverter<T> implements MessageConverter<T> {
    private org.springframework.amqp.support.converter.MessageConverter messageConverter = new SimpleMessageConverter();
    private MessageConverter<T> converter = new DefaultMessageConverter();

    @Override
    public MessageWrap<T> convert(T message) throws MessageConverterException {
        Object obj = this.messageConverter.fromMessage((Message)message);
        if (obj != null) {
            return this.converter.convert((T)obj);
        }
        throw new MessageConverterException("%s 通过 %s 转换成 MessageWrap失败", message, getClass().getSimpleName());
    }
}
