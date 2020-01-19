package org.springframework.amqp.rabbit.ext.conf;

/**
 * @ClassName SimpleShouldIdempotentCheckService
 * @Author wuwei
 * @Description NONE
 * @Date 2020/1/19 16:05
 **/
public class SimpleShouldIdempotentCheckService implements ShouldIdempotentCheckService {
    @Override
    public Boolean shouldCheck(String id) {
        return SpringRetryMessageCorrelationHolder.get(id);
    }
}
