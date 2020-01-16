package org.springframework.amqp.rabbit.ext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;

import com.rabbitmq.client.Channel;

/**
 * @ClassName MqUtils
 * @Author wuwei
 * @Description
 * @Date 2019/12/20 13:55
 **/
public class MqUtils {
    private static final Logger logger = LoggerFactory.getLogger(MqUtils.class);

    /**
     * 手动ack
     * 
     * @param message
     * @param channel
     */
    public static void ack(Message message, Channel channel) {
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            logger.error("ack消息异常", e);
            throw new RuntimeException("ack异常，mq将重新发送该消息");
        }
    }

    /**
     * 休眠1秒后recover消息
     * 
     * @param channel
     * @param second
     * @throws Exception
     */
    public static void recover(Channel channel, Integer second) throws Exception {
        if (second != null && second.intValue() > 0) {
            Thread.sleep((1000 * second.intValue()));
        }
        channel.basicRecover();
    }
}
