package org.springframework.amqp.rabbit.ext.convert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.ext.MessageWrap;

/**
 * @ClassName DefaultMessageConverter
 * @Author wuwei
 * @Description
 * @Date 2019/12/20 14:08
 **/
public class DefaultMessageConverter<T> implements MessageConverter<T> {
    private static Logger logger = LoggerFactory.getLogger(DefaultMessageConverter.class);

    @Override
    public MessageWrap<T> convert(T message) throws MessageConverterException {
        if (message != null) {
            if (message instanceof MessageWrap) {
                return (MessageWrap)message;
            }
            return new MessageWrap(message);
        }
        throw new MessageConverterException("%s 通过 %s 转换成 MessageWrap失败", message, getClass().getSimpleName());
    }
}
