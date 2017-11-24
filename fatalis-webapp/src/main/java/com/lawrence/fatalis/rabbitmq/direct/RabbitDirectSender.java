package com.lawrence.fatalis.rabbitmq.direct;

import com.lawrence.fatalis.config.rabbitmq.direct.RabbitDirectConfig;
import com.lawrence.fatalis.test.TestObj;
import com.lawrence.fatalis.util.LogUtil;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * rabbitmq消息发送者
 */
@Component
@ConditionalOnExpression("'${fatalis.rabbitmq-open}' == 'direct' or '${fatalis.rabbitmq-open}' == 'true'")
public class RabbitDirectSender {

    @Resource
    private AmqpTemplate rabbitTemplate;


    /**
     * 发送消息
     */
    public void sendMessage(TestObj message) {
        rabbitTemplate.convertAndSend(RabbitDirectConfig.DIRECT_QUEUE, message);

        LogUtil.info(getClass(), "消息队列" + RabbitDirectConfig.DIRECT_QUEUE + "发送: " + message.toString());

    }

}
