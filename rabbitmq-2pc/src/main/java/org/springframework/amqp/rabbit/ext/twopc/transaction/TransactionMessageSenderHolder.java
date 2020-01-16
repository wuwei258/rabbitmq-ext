package org.springframework.amqp.rabbit.ext.twopc.transaction;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName TransactionMessageSenderHolder
 * @Author wuwei
 * @Description NONE
 * @Date 2020/1/13 16:59
 **/
public class TransactionMessageSenderHolder implements InitializingBean, ApplicationContextAware {

    private static ApplicationContext applicationContext;
    private static final Map<String, AbstractTransactionMessageSender> HOLDER = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    @Override
    public void afterPropertiesSet() {
        assertApplicationContext();
        Map<String, AbstractTransactionMessageSender> beansOfType =
            applicationContext.getBeansOfType(AbstractTransactionMessageSender.class);
        Collection<AbstractTransactionMessageSender> values = beansOfType.values();
        for (AbstractTransactionMessageSender value : values) {
            HOLDER.put(value.queue(), value);
        }
    }

    public static AbstractTransactionMessageSender transactionMessageSender(String queue) {
        assertApplicationContext();
        return HOLDER.get(queue);
    }

    private static void assertApplicationContext() {
        if (applicationContext == null) {
            throw new RuntimeException("applicationContext属性为null,请检查是否注入了TransactionMessageSenderHolder!");
        }
    }
}
