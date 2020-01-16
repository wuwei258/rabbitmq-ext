package org.springframework.amqp.rabbit.ext.sender;

import org.springframework.amqp.rabbit.ext.MessageWrap;

/**
 * @ClassName MqSenderService
 * @Author wuwei
 * @Description
 * @Date 2019/12/20 14:57
 **/
public interface MqSenderService<T> {
    /**
     * 发送消息
     *
     * @param paramT
     */
    void send(T paramT);


    /**
     * 发送确认消息
     *
     * @param param
     * @param correlationDataId
     * @return
     */
    MessageWrap<T> sendConfirm(T param, String correlationDataId);

    /**
     * 发送确认消息
     *
     * @param param
     * @param correlationDataId
     * @param queue
     * @return
     */
    MessageWrap<T> sendConfirm(T param, String correlationDataId, String queue);

    /**
     * 发送消息至指定队列
     *
     * @param paramT
     * @param queue
     */
    void send(T paramT, String queue);

}
