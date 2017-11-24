package com.lawrence.fatalis.rabbitmq;

import com.lawrence.fatalis.config.rabbitmq.AmqpConfig;
import com.lawrence.fatalis.test.TestObj;
import com.lawrence.fatalis.util.LogUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnExpression("${fatalis.rabbitmq-open}")
@RabbitListener(queues = AmqpConfig.TOPIC_QUEUE2)
public class TopicReceiver2 {

    @RabbitHandler
    public void receiveMessage(@Payload TestObj message) {

        LogUtil.info(getClass(), "消息队列" + AmqpConfig.TOPIC_QUEUE2 + "接收: " + message.toString());

        // 设定抛出异常, 测试default-requeue-rejected: false
        /*String str = null;
        str.toString();*/

    }

}
