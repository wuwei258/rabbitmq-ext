package org.springframework.amqp.rabbit.ext.conf;

import lombok.Data;
import org.aopalliance.aop.Advice;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.config.StatefulRetryOperationsInterceptorFactoryBean;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.ext.ListenerSelector;
import org.springframework.amqp.rabbit.ext.idempotent.IdempotentService;
import org.springframework.amqp.rabbit.ext.interceptor.IdempotentInterceptor;
import org.springframework.amqp.rabbit.ext.interceptor.ManualAckRetryInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.retry.RetryListener;
import org.springframework.retry.interceptor.StatefulRetryOperationsInterceptor;
import org.springframework.retry.policy.MapRetryContextCache;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.amqp.rabbit.ext.MqConstants.DEFAULT_MQ_ERROR_HANDLER;
import static org.springframework.amqp.rabbit.ext.MqConstants.RETRY_TIMES;

/**
 * @ClassName RabbitConfiguration
 * @Author wuwei
 * @Description
 * @Date 2019/12/20 14:28
 **/

@EnableRabbit
@Configuration
@EnableConfigurationProperties(RabbitMqProperties.class)
public class RabbitConfiguration {

    @Resource
    private RabbitMqProperties rabbitMqProperties;
    @Resource(name = DEFAULT_MQ_ERROR_HANDLER)
    private MqErrorHandler errorHandler;

    @Bean
    @ConditionalOnMissingBean(ConnectionFactory.class)
    public ConnectionFactory rabbitConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(rabbitMqProperties.getHost());
        connectionFactory.setPort(Integer.valueOf(rabbitMqProperties.getPort()).intValue());
        connectionFactory.setUsername(rabbitMqProperties.getUsername());
        connectionFactory.setPassword(rabbitMqProperties.getPassword());
        connectionFactory.setVirtualHost(rabbitMqProperties.getVhost());
        connectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CHANNEL);
        connectionFactory.setChannelCacheSize(25);
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        connectionFactory.setPublisherReturns(true);
        connectionFactory.setRequestedHeartBeat(500);
        connectionFactory.setConnectionTimeout(30000);
        return connectionFactory;
    }

    @Bean
    @ConditionalOnMissingBean(AmqpAdmin.class)
    public AmqpAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        rabbitAdmin.setIgnoreDeclarationExceptions(false);
        return rabbitAdmin;
    }

    @Bean
    @ConditionalOnMissingBean({RabbitTemplate.class, RabbitTemplate.ConfirmCallback.class})
    public RabbitTemplate rabbitTemplate(RetryTemplate retryTemplate, ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setRetryTemplate(retryTemplate);
        rabbitTemplate.setReplyTimeout(30000L);
        rabbitTemplate.setExchange(rabbitMqProperties.getDefaultExchange());
        return rabbitTemplate;
    }

    @Bean
    @ConditionalOnMissingBean({RabbitTemplate.class})
    @ConditionalOnBean(RabbitTemplate.ConfirmCallback.class)
    public RabbitTemplate confirmRabbitTemplate(RabbitTemplate.ConfirmCallback confirmCallback,
        RetryTemplate retryTemplate, ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setRetryTemplate(retryTemplate);
        rabbitTemplate.setReplyTimeout(30000L);
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setExchange(rabbitMqProperties.getDefaultExchange());
        return rabbitTemplate;
    }

    @Bean
    @ConditionalOnMissingBean(Exchange.class)
    public Exchange defaultExchange() {
        return ExchangeBuilder.directExchange(rabbitMqProperties.getDefaultExchange()).durable(true).build();
    }

    @Bean
    @ConditionalOnMissingBean(AbstractMessageRetryListener.class)
    public AbstractMessageRetryListener messageRetryListener() {
        return new SimpleMessageRetryListener();
    }

    @Bean
    @ConditionalOnMissingBean(ShouldIdempotentCheckService.class)
    public ShouldIdempotentCheckService shouldIdempotentCheckService() {
        return new SimpleShouldIdempotentCheckService();
    }

    @Bean
    @ConditionalOnMissingBean({RetryTemplate.class, AbstractMessageRetryListener.class})
    @DependsOn({"mapRetryContextCache", "messageRetryListener"})
    public RetryTemplate retryTemplate(MapRetryContextCache mapRetryContextCache,
        AbstractMessageRetryListener abstractMessageRetryListener) {
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryContextCache(mapRetryContextCache);
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(RETRY_TIMES);
        retryTemplate.setRetryPolicy(retryPolicy);
        retryTemplate.setListeners(new RetryListener[] {abstractMessageRetryListener});
        return retryTemplate;
    }

    @Bean
    @ConditionalOnMissingBean(MapRetryContextCache.class)
    public MapRetryContextCache mapRetryContextCache() {
        return new MapRetryContextCache();
    }

    @Bean
    @ConditionalOnMissingBean(ListenerAdvice.class)
    public ListenerAdvice listenerAdvice(IdempotentService idempotentService, RetryTemplate retryTemplate,
        ShouldIdempotentCheckService idempotentCheckService) {
        ListenerContainerFactoryAdvice advice = new ListenerContainerFactoryAdvice();
        StatefulRetryOperationsInterceptorFactoryBean interceptor = new StatefulRetryOperationsInterceptorFactoryBean();
        interceptor.setRetryOperations(retryTemplate);
        StatefulRetryOperationsInterceptor retryOperationsInterceptor = interceptor.getObject();
        ManualAckRetryInterceptor manualAckRetryInterceptor = new ManualAckRetryInterceptor(true);
        IdempotentInterceptor idempotentInterceptor =
            new IdempotentInterceptor(idempotentService, idempotentCheckService);
        advice.addAdvice(idempotentInterceptor);
        advice.addAutoAdvice(retryOperationsInterceptor);
        advice.addManualAdvice(manualAckRetryInterceptor);
        return advice;
    }

    @Bean
    @ConditionalOnClass(ListenerSelector.class)
    @DependsOn("listenerAdvice")
    public SimpleRabbitListenerContainerFactory multiThreadListenerContainerFactory(ListenerAdvice advice) {
        return createListenerContainerFactory(
            new ListenerContainerFactoryRegister(AcknowledgeMode.AUTO, Integer.valueOf(10), Integer.valueOf(30),
                Integer.valueOf(10), Integer.valueOf(10), advice));
    }

    @Bean
    @ConditionalOnClass(ListenerSelector.class)
    @DependsOn("listenerAdvice")
    public SimpleRabbitListenerContainerFactory multiThreadTo20ListenerContainerFactory(ListenerAdvice advice) {
        return createListenerContainerFactory(
            new ListenerContainerFactoryRegister(AcknowledgeMode.AUTO, Integer.valueOf(20), Integer.valueOf(60),
                Integer.valueOf(10), Integer.valueOf(10), advice));
    }

    @Bean
    @ConditionalOnClass(ListenerSelector.class)
    @DependsOn("listenerAdvice")
    public SimpleRabbitListenerContainerFactory multiThreadTo50ListenerContainerFactory(ListenerAdvice advice) {
        return createListenerContainerFactory(
            new ListenerContainerFactoryRegister(AcknowledgeMode.AUTO, Integer.valueOf(50), Integer.valueOf(150),
                Integer.valueOf(10), Integer.valueOf(10), advice));
    }

    @Bean
    @ConditionalOnClass(ListenerSelector.class)
    @DependsOn("listenerAdvice")
    public SimpleRabbitListenerContainerFactory multiThreadAndManualAcknowledgeListenerContainerFactory(
        ListenerAdvice advice) {
        return createListenerContainerFactory(
            new ListenerContainerFactoryRegister(AcknowledgeMode.MANUAL, Integer.valueOf(3), Integer.valueOf(10),
                Integer.valueOf(10), Integer.valueOf(10), advice));
    }

    @Bean
    @ConditionalOnClass(ListenerSelector.class)
    @DependsOn("listenerAdvice")
    public SimpleRabbitListenerContainerFactory singleThreadListenerContainerFactory(ListenerAdvice advice) {
        return createListenerContainerFactory(
            new ListenerContainerFactoryRegister(AcknowledgeMode.AUTO, Integer.valueOf(1), Integer.valueOf(1),
                Integer.valueOf(1), Integer.valueOf(3), advice));
    }

    @Bean
    @ConditionalOnClass(ListenerSelector.class)
    @DependsOn("listenerAdvice")
    public SimpleRabbitListenerContainerFactory singleThreadAndManualAcknowledgeListenerContainerFactory(
        ListenerAdvice advice) {
        return createListenerContainerFactory(
            new ListenerContainerFactoryRegister(AcknowledgeMode.MANUAL, Integer.valueOf(1), Integer.valueOf(1),
                Integer.valueOf(1), Integer.valueOf(3), advice));
    }

    private SimpleRabbitListenerContainerFactory createListenerContainerFactory(
        ListenerContainerFactoryRegister register) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(rabbitConnectionFactory());
        factory.setAcknowledgeMode(register.getAckMode());
        factory.setConcurrentConsumers(register.getConcurrentConsumers());
        factory.setMaxConcurrentConsumers(register.getMaxConcurrentConsumers());
        /*factory.setTxSize(register.getTxSize());
        factory.setChannelTransacted(Boolean.valueOf(true));*/
        factory.setPrefetchCount(register.getPerFetch());
        factory.setAutoStartup(Boolean.valueOf(true));
        factory.setErrorHandler(this.errorHandler);
        ListenerAdvice advice = register.getAdvice();
        List<Advice> totalAdvice = new ArrayList<>();
        if (!CollectionUtils.isEmpty(advice.getAdvice())) {
            totalAdvice.addAll(advice.getAdvice());
        }
        switch (register.getAckMode()) {
            case MANUAL:
                if (!CollectionUtils.isEmpty(advice.getManualAdvice())) {
                    totalAdvice.addAll(advice.getManualAdvice());
                }
                break;
            default:
                if (!CollectionUtils.isEmpty(advice.getAutoAdvice())) {
                    totalAdvice.addAll(advice.getAutoAdvice());
                }
        }
        factory.setAdviceChain(totalAdvice.toArray(new Advice[totalAdvice.size()]));
        return factory;
    }

    @Bean
    @ConditionalOnClass(ListenerSelector.class)
    public Advice retryOperationsInterceptor(RetryTemplate retryTemplate) {
        StatefulRetryOperationsInterceptorFactoryBean interceptor = new StatefulRetryOperationsInterceptorFactoryBean();
        interceptor.setRetryOperations(retryTemplate);
        return interceptor.getObject();
    }

    @Data
    class ListenerContainerFactoryRegister {
        private AcknowledgeMode ackMode;

        private Integer concurrentConsumers;

        private Integer maxConcurrentConsumers;

        private Integer txSize;

        private Integer perFetch;

        private ListenerAdvice advice;

        public ListenerContainerFactoryRegister(AcknowledgeMode ackMode, Integer concurrentConsumers,
            Integer maxConcurrentConsumers, Integer txSize, Integer perFetch, ListenerAdvice advice) {
            this.ackMode = ackMode;
            this.concurrentConsumers = concurrentConsumers;
            this.maxConcurrentConsumers = maxConcurrentConsumers;
            this.txSize = txSize;
            this.perFetch = perFetch;
            this.advice = advice;
        }

    }
}
