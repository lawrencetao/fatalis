package com.lawrence.fatalis.task;

import com.lawrence.fatalis.util.LogUtil;
import com.lawrence.fatalis.util.StringUtil;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 *  动态cron表达式Schedule定时任务
 */
@Component
@Configuration
@ConfigurationProperties(prefix = "schedule.dynamicTask")
@Data
public class DynamicScheduleTask implements SchedulingConfigurer {

    private String cron;

    /**
     * 配置定时任务
     *
     * @param taskRegister
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegister) {

        // 当前触发器cron表达式
        if (StringUtil.isNull(cron)) {
            throw new NullPointerException("定时cron表达式不能为空");
        }

        taskRegister.addTriggerTask(() -> {

            LogUtil.info(getClass(), "动态定时任务, 每一段时间执行一次, 或定时执行");

        }, triggerContext -> {

            // 定时任务触发, 可修改定时任务的执行周期
            CronTrigger trigger = new CronTrigger(cron);
            Date nextExecDate = trigger.nextExecutionTime(triggerContext);

            return nextExecDate;
        });

    }

}
