package org.springframework.amqp.rabbit.ext.spring;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;



@Component
public class ExchangeRegisar implements BeanPostProcessor, BeanFactoryAware {
    private BeanFactory beanFactory;

    @Resource(name = "defaultExchange")
    private Exchange exchange;
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Queue) {
            registerBeans(bean);
        }
        return bean;
    }

    private void registerBeans(Object bean) {
        Queue queue = (Queue) bean;
        String queueName = queue.getName();
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(queueName).noargs();
        ((DefaultListableBeanFactory) this.beanFactory).registerSingleton("binding" + queueName, binding);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
