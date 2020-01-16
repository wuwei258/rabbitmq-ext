package org.springframework.amqp.rabbit.ext.convert;

import org.springframework.amqp.rabbit.ext.MessageWrap;

/**
 * @ClassName MessageConverter
 * @Author wuwei
 * @Description
 * @Date 2019/12/20 14:03
 **/
public interface MessageConverter<T> {
    /**
     * 消息转换
     *
     * @param paramT
     * @return
     * @throws MessageConverterException
     */
    MessageWrap<T> convert(T paramT) throws MessageConverterException;
}
