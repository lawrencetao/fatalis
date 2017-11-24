package com.lawrence.fatalis.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfig {

    public static final String TOPIC_EXCHANGE = "topicExchange";
    public static final String TOPIC_QUEUE1 = "topicQueue1";
    public static final String TOPIC_QUEUE2 = "topicQueue2";

    /**
     * 队列topicQueue1配置
     *
     * @return Queue
     */
    @Bean
    public Queue topicQueue1() {

        return new Queue(TOPIC_QUEUE1);
    }

    /**
     * 队列topicQueue2配置
     *
     * @return Queue
     */
    @Bean
    public Queue topicQueue2() {

        return new Queue(TOPIC_QUEUE2);
    }

    /**
     * 交换器exchange配置
     *
     * @return TopicExchange
     */
    @Bean
    public TopicExchange exchange() {

        return new TopicExchange(TOPIC_EXCHANGE);
    }

    /**
     * 队列topicQueue1绑定到交换器exchange
     *
     * @return Binding
     */
    @Bean
    public Binding bindingExchange1(Queue topicQueue1, TopicExchange exchange) {

        return BindingBuilder.bind(topicQueue1).to(exchange).with("topic.queue1");
    }

    /**
     * 队列topicQueue2绑定到交换器exchange
     *
     * @return TopicExchange
     */
    @Bean
    public Binding bindingExchange2(Queue topicQueue2, TopicExchange exchange) {

        return BindingBuilder.bind(topicQueue2).to(exchange).with("topic.#");
    }

    /**
     * 队列topicQueue2绑定到交换器exchange
     *
     * @return TopicExchange
     */
    @Bean
    public Binding bindingExchange3(Queue topicQueue2, TopicExchange exchange) {

        return BindingBuilder.bind(topicQueue2).to(exchange).with("*.queue2");
    }

}
