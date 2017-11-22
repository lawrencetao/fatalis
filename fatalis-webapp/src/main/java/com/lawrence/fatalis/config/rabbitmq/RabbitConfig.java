package com.lawrence.fatalis.config.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * rabbitmq配置, direct模式
 */
@Configuration
@ConditionalOnProperty(prefix = "fatalis", name = "rabbitmq-open", havingValue = "true")
public class RabbitConfig {

    public static final String DIRECT_QUEUE = "directQueue";

    /**
     * 队列dateQueue配置
     *
     * @return Queue
     */
    @Bean
    public Queue dateQueue() {

        return new Queue(DIRECT_QUEUE);
    }

}
