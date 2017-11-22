package com.lawrence.fatalis.task;

import com.lawrence.fatalis.util.LogUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Schedule定时任务
 */
/*@Component*/
public class ScheduleTask {

    public final static long SECOND = 1000;// 单位: 秒
    public final static long MINUTE = 60 * 1000;// 单位: 分
    public final static long HOUR = 60 * 60 * 1000;// 单位: 时

    public final static String CRON_STR = "0/15 * * * * ?";// cron表达式

    /**
     * 每隔一段时间执行, 执行完毕开始下一次计时
     */
    @Scheduled(fixedDelay = SECOND * 5)
    public void fixedDelayJob() {

        LogUtil.info(getClass(), "每隔5秒运行一次, 执行完成开始计时");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * 每一段时间执行, 执行开始立刻开始下一次计时
     */
    @Scheduled(fixedRate = SECOND * 10)
    public void fixedRateJob() {

        LogUtil.info(getClass(), "每隔10秒运行一次, 执行开始即计时");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据cron表达式, 定时执行
     */
    @Scheduled(cron = CRON_STR)
    public void cronJob() {

        LogUtil.info(getClass(), "每隔15秒运行一次, 执行开始即计时");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
