package org.springframework.amqp.rabbit.ext.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.ext.MqConstants;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

/**
 * @ClassName MqErrorHandler
 * @Author wuwei
 * @Description
 * @Date 2019/12/20 14:28
 **/
@Component(MqConstants.DEFAULT_MQ_ERROR_HANDLER)
public class MqErrorHandler implements ErrorHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void handleError(Throwable t) {
        this.logger.error("MQ 发生异常 : {}" + t.getMessage(), t);
    }
}
