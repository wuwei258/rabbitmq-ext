package org.springframework.amqp.rabbit.ext.conf;

import org.aopalliance.aop.Advice;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ListenerContainerFactoryAdivce
 * @Author wuwei
 * @Description NONE
 * @Date 2020/1/19 9:09
 **/
public class ListenerContainerFactoryAdvice implements ListenerAdvice {
    /**
     * 通用拦截器
     */
    private List<Advice> advice;
    /**
     * 自动ack拦截器
     */
    private List<Advice> autoAckAdvice;
    /**
     * 手动ack拦截器
     */
    private List<Advice> manualAckAdvice;

    @Override
    public List<Advice> getAdvice() {
        return advice;
    }

    @Override
    public List<Advice> getAutoAdvice() {
        return autoAckAdvice;
    }

    @Override
    public List<Advice> getManualAdvice() {
        return manualAckAdvice;
    }

    /**
     * 添加拦截器
     *
     * @param methodInterceptor
     */
    @Override
    public void addAdvice(Advice methodInterceptor) {
        if (CollectionUtils.isEmpty(advice)) {
            advice = new ArrayList<>();
        }
        advice.add(methodInterceptor);
    }

    /**
     * 添加自动ack拦截器
     *
     * @param methodInterceptor
     */
    @Override
    public void addAutoAdvice(Advice methodInterceptor) {
        if (CollectionUtils.isEmpty(autoAckAdvice)) {
            autoAckAdvice = new ArrayList<>();
        }
        autoAckAdvice.add(methodInterceptor);
    }

    /**
     * 添加手动ack拦截器
     *
     * @param methodInterceptor
     */
    @Override
    public void addManualAdvice(Advice methodInterceptor) {
        if (CollectionUtils.isEmpty(manualAckAdvice)) {
            manualAckAdvice = new ArrayList<>();
        }
        manualAckAdvice.add(methodInterceptor);
    }
}
