package com.lawrence.fatalis.rabbitmq.topic;

import com.lawrence.fatalis.config.rabbitmq.topic.RabbitTopicConfig;
import com.lawrence.fatalis.test.TestObj;
import com.lawrence.fatalis.util.LogUtil;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@ConditionalOnExpression("'${fatalis.rabbitmq-open}' == 'topic' or '${fatalis.rabbitmq-open}' == 'true'")
public class RabbitTopicSender {

    @Resource
    private AmqpTemplate rabbitTemplate;

    /**
     * 发送消息
     */
    public void sendMessage1(TestObj message) {
        rabbitTemplate.convertAndSend(RabbitTopicConfig.TOPIC_EXCHANGE, "topic.queue1", message);

        LogUtil.info(getClass(), "消息队列RoutingKey: topic.queue1, 发送: " + message.toString());

    }
    public void sendMessage2(TestObj message) {
        rabbitTemplate.convertAndSend(RabbitTopicConfig.TOPIC_EXCHANGE, "topic.any", message);

        LogUtil.info(getClass(), "消息队列RoutingKey: topic.any, 发送: " + message.toString());

    }
    public void sendMessage3(TestObj message) {
        rabbitTemplate.convertAndSend(RabbitTopicConfig.TOPIC_EXCHANGE, "any.queue2", message);

        LogUtil.info(getClass(), "消息队列RoutingKey: any.queue2, 发送: " + message.toString());

    }

}
