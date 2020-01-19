package org.springframework.amqp.rabbit.ext;

/**
 * @ClassName MqConstans
 * @Author wuwei
 * @Description NONE
 * @Date 2019/12/20 14:36
 **/
public interface MqConstants {
    /**
     * 默认的mq异常队列handler
     */
    String DEFAULT_MQ_ERROR_HANDLER = "defaultMqErrorHandler";

    String CORRELATION_ID = "spring_returned_message_correlation";
    String RETRY_HEADER = "retry_times_header";

    /**
     * 重试次数
     */
    Integer RETRY_TIMES = 3;
}
