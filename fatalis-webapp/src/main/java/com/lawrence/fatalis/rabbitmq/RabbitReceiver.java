package com.lawrence.fatalis.rabbitmq;

import com.lawrence.fatalis.util.LogUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * rabbitmq消息接收者
 */
@Component
@ConditionalOnProperty(prefix = "fatalis", name = "rabbitmq-open", havingValue = "true")
@RabbitListener(queues = "dateQueue")
public class RabbitReceiver {

    @RabbitHandler
    public void receiveMessage(String message) {

        LogUtil.info(getClass(), "消息队列dateQueue接收: " + message);

    }

}
