package org.springframework.amqp.rabbit.ext.twopc.conf;

import org.aopalliance.aop.Advice;
import org.springframework.amqp.rabbit.config.StatefulRetryOperationsInterceptorFactoryBean;
import org.springframework.amqp.rabbit.ext.MqConstants;
import org.springframework.amqp.rabbit.ext.conf.ListenerAdvice;
import org.springframework.amqp.rabbit.ext.conf.ListenerContainerFactoryAdvice;
import org.springframework.amqp.rabbit.ext.conf.RabbitConfiguration;
import org.springframework.amqp.rabbit.ext.conf.ShouldIdempotentCheckService;
import org.springframework.amqp.rabbit.ext.idempotent.IdempotentService;
import org.springframework.amqp.rabbit.ext.interceptor.IdempotentInterceptor;
import org.springframework.amqp.rabbit.ext.interceptor.ManualAckRetryInterceptor;
import org.springframework.amqp.rabbit.ext.twopc.interceptor.TransactionMessageInterceptor;
import org.springframework.amqp.rabbit.ext.twopc.transaction.service.InMemoryTransMessageService;
import org.springframework.amqp.rabbit.ext.twopc.transaction.service.TransMessageService;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.retry.RetryListener;
import org.springframework.retry.interceptor.StatefulRetryOperationsInterceptor;
import org.springframework.retry.policy.MapRetryContextCache;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

/**
 * @ClassName TransactionRabbitMqConfig
 * @Author wuwei
 * @Description NONE
 * @Date 2020/1/13 16:20
 **/
@Configuration
@AutoConfigureBefore(RabbitConfiguration.class)
public class TransactionRabbitMqConfig {

    @Bean
    @ConditionalOnMissingBean(TransMessageService.class)
    public TransMessageService transMessageService() {
        return new InMemoryTransMessageService();
    }

    @Bean
    public TransactionMessageInterceptor transactionMessageInterceptor() {
        return new TransactionMessageInterceptor();
    }

    @Bean
    @DependsOn("transMessageService")
    public TransactionAbstractMessageRetryListener messageRetryListener(TransMessageService transMessageService) {
        return new TransactionAbstractMessageRetryListener(transMessageService);
    }

    @Bean
    @ConditionalOnMissingBean(ShouldIdempotentCheckService.class)
    @DependsOn("transMessageService")
    public ShouldIdempotentCheckService shouldIdempotentCheckService(TransMessageService transMessageService) {
        return new TransactionMessageIdempotentCheckService(transMessageService);
    }

    @Bean
    @ConditionalOnMissingBean(ListenerAdvice.class)
    public ListenerAdvice listenerAdvice(IdempotentService idempotentService,
        TransactionMessageInterceptor transactionMessageInterceptor, Advice autoRetryAdvice,
        ShouldIdempotentCheckService shouldIdempotentCheckService) {
        ListenerContainerFactoryAdvice advice = new ListenerContainerFactoryAdvice();
        ManualAckRetryInterceptor manualAckRetryInterceptor = new ManualAckRetryInterceptor(true);
        IdempotentInterceptor idempotentInterceptor =
            new IdempotentInterceptor(idempotentService, shouldIdempotentCheckService);
        advice.addAdvice(idempotentInterceptor);
        advice.addAdvice(transactionMessageInterceptor);
        advice.addAutoAdvice(autoRetryAdvice);
        advice.addManualAdvice(manualAckRetryInterceptor);
        return advice;
    }

    @Bean
    @DependsOn("retryTemplate")
    public Advice autoRetryAdvice(RetryTemplate retryTemplate) {
        StatefulRetryOperationsInterceptorFactoryBean interceptor = new StatefulRetryOperationsInterceptorFactoryBean();
        interceptor.setRetryOperations(retryTemplate);
        StatefulRetryOperationsInterceptor retryOperationsInterceptor = interceptor.getObject();
        return retryOperationsInterceptor;
    }

    @Bean
    @ConditionalOnMissingBean(RetryTemplate.class)
    public RetryTemplate retryTemplate(MapRetryContextCache mapRetryContextCache,
        TransactionAbstractMessageRetryListener messageRetryListener) {
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryContextCache(mapRetryContextCache);
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(MqConstants.RETRY_TIMES);
        retryTemplate.setRetryPolicy(retryPolicy);
        /*ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        retryTemplate.setBackOffPolicy(backOffPolicy);*/
        retryTemplate.setListeners(new RetryListener[] {messageRetryListener});
        return retryTemplate;
    }

    @Bean
    @ConditionalOnMissingBean(MapRetryContextCache.class)
    public MapRetryContextCache mapRetryContextCache() {
        return new MapRetryContextCache();
    }
}
