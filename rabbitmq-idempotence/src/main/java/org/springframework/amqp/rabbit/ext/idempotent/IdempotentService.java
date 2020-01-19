package org.springframework.amqp.rabbit.ext.idempotent;

/**
 * @className IdempotentService
 * @author wuwei
 * @description  幂等性校验接口
 * @date  2020/1/16 11:41
 **/
public interface IdempotentService {

    /**
     * 幂等校验
     *
     * @param business 业务编码
     * @param key      校验key
     * @return
     */
    boolean idempotentCheck(String business, String key);


}
