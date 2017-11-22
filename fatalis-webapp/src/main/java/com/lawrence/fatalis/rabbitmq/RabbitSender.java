package com.lawrence.fatalis.rabbitmq;

import com.lawrence.fatalis.config.rabbitmq.RabbitConfig;
import com.lawrence.fatalis.test.TestObj;
import com.lawrence.fatalis.util.LogUtil;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * rabbitmq消息发送者
 */
@Component
@ConditionalOnProperty(prefix = "fatalis", name = "rabbitmq-open", havingValue = "true")
public class RabbitSender {

    @Resource
    private AmqpTemplate rabbitTemplate;


    /**
     * 发送消息
     */
    public void sendMessage(TestObj message) {
        rabbitTemplate.convertAndSend(RabbitConfig.DIRECT_QUEUE, message);

        LogUtil.info(getClass(), "消息队列" + RabbitConfig.DIRECT_QUEUE + "发送: " + message.toString());

    }

}
