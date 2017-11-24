package com.lawrence.fatalis.config.rabbitmq.direct;

import org.springframework.amqp.core.Queue;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * rabbitmq配置, direct模式
 */
@Configuration
@ConditionalOnExpression("'${fatalis.rabbitmq-open}' == 'direct' or '${fatalis.rabbitmq-open}' == 'true'")
public class RabbitDirectConfig {

    public static final String DIRECT_QUEUE = "directQueue";

    /**
     * 队列directQueue配置
     *
     * @return Queue
     */
    @Bean
    public Queue directQueue() {

        return new Queue(DIRECT_QUEUE);
    }

}
