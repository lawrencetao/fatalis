package com.lawrence.fatalis.rabbitmq;

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
    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend("dateQueue", message);

        LogUtil.info(getClass(), "消息队列dateQueue发送: " + message);

    }

}
