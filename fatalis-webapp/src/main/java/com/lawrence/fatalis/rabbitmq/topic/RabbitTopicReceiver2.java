package com.lawrence.fatalis.rabbitmq.topic;

import com.lawrence.fatalis.config.rabbitmq.topic.RabbitTopicConfig;
import com.lawrence.fatalis.test.TestObj;
import com.lawrence.fatalis.util.LogUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnExpression("'${fatalis.rabbitmq-open}' == 'topic' or '${fatalis.rabbitmq-open}' == 'true'")
@RabbitListener(queues = RabbitTopicConfig.TOPIC_QUEUE_2)
public class RabbitTopicReceiver2 {

    @RabbitHandler
    public void receiveMessage(TestObj message) {

        LogUtil.info(getClass(), "消息队列" + RabbitTopicConfig.TOPIC_QUEUE_2 + "接收: " + message.toString());

    }

}
