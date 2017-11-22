package com.lawrence.fatalis.rabbitmq;

import com.lawrence.fatalis.config.rabbitmq.RabbitConfig;
import com.lawrence.fatalis.test.TestObj;
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
@RabbitListener(queues = RabbitConfig.DIRECT_QUEUE)
public class RabbitReceiver {

    @RabbitHandler
    public void receiveMessage(TestObj message) {

        LogUtil.info(getClass(), "消息队列" + RabbitConfig.DIRECT_QUEUE + "接收: " + message.toString());

    }

}
