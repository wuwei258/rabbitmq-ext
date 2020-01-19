package org.springframework.amqp.rabbit.ext.conf;

/**
 * @author wuwei
 * @className ShouldIdempotentCheckService
 * @description
 * @date 2020/1/19 16:05
 **/
public interface ShouldIdempotentCheckService {

    /**
     * 该消息是否需要幂等校验
     *
     * @param id
     * @return
     */
    Boolean shouldCheck(String id);
}
