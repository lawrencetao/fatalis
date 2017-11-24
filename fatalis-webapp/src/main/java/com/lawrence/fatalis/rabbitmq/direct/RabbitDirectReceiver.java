package com.lawrence.fatalis.rabbitmq.direct;

import com.lawrence.fatalis.config.rabbitmq.direct.RabbitDirectConfig;
import com.lawrence.fatalis.test.TestObj;
import com.lawrence.fatalis.util.LogUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * rabbitmq消息接收者
 */
@Component
@ConditionalOnExpression("'${fatalis.rabbitmq-open}' == 'direct' or '${fatalis.rabbitmq-open}' == 'true'")
@RabbitListener(queues = RabbitDirectConfig.DIRECT_QUEUE)
public class RabbitDirectReceiver {

    @RabbitHandler
    public void receiveMessage(TestObj message) {

        LogUtil.info(getClass(), "消息队列" + RabbitDirectConfig.DIRECT_QUEUE + "接收: " + message.toString());

    }

}
