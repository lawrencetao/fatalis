package com.lawrence.fatalis.rabbitmq;

import com.lawrence.fatalis.config.rabbitmq.AmqpConfig;
import com.lawrence.fatalis.test.TestObj;
import com.lawrence.fatalis.util.LogUtil;
import com.lawrence.fatalis.util.StringUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.stereotype.Component;

@Component
public class TopicSender2 implements RabbitTemplate.ConfirmCallback {

    private RabbitTemplate rabbitTemplate;

    public TopicSender2(RabbitTemplate rabbitTemp) {
        rabbitTemplate = rabbitTemp;
        rabbitTemplate.setConfirmCallback(this);
    }

    public void sendMessage1(TestObj message) {
        CorrelationData correlationData = new CorrelationData(StringUtil.getUUIDStr());
        rabbitTemplate.convertAndSend(AmqpConfig.TOPIC_EXCHANGE, "topic.queue2", message, correlationData);
    }

    /**
     * 回调方法
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean status, String message) {

        LogUtil.info(getClass(), "消息回调id: " + correlationData.getId() + ", 状态: " + status + ", 消息: " + message);

    }
}
