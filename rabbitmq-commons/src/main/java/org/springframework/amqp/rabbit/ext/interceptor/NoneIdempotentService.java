package org.springframework.amqp.rabbit.ext.interceptor;

import org.springframework.amqp.rabbit.ext.idempotent.IdempotentService;

/**
 * @ClassName NoneIdempotentService
 * @Author wuwei
 * @Description 不做幂等判断
 * @Date 2020/1/16 15:29
 **/
public class NoneIdempotentService implements IdempotentService {
    @Override
    public boolean idempotentCheck(String business, String key) {
        return true;
    }
}
