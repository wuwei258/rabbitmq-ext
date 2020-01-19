package org.springframework.amqp.rabbit.ext.conf;

import org.aopalliance.aop.Advice;

import java.util.List;

/**
 * @author wuwei
 * @className ListenerAdvice
 * @description
 * @date 2020/1/19 9:43
 **/
public interface ListenerAdvice {

    /**
     * 获取通用拦截
     *
     * @return
     */
    List<Advice> getAdvice();

    /**
     * 获取自动ack拦截
     *
     * @return
     */
    List<Advice> getAutoAdvice();

    /**
     * 获取手动ack拦截
     *
     * @return
     */
    List<Advice> getManualAdvice();

    /**
     * 添加拦截器
     *
     * @param methodInterceptor
     */
    void addAdvice(Advice methodInterceptor);

    /**
     * 添加自动ack拦截器
     *
     * @param methodInterceptor
     */
    void addAutoAdvice(Advice methodInterceptor);

    /**
     * 添加手动ack拦截器
     *
     * @param methodInterceptor
     */
    void addManualAdvice(Advice methodInterceptor);
}
