package com.lawrence.fatalis.listener;

import com.lawrence.fatalis.util.LogUtil;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * spring容器监听器
 */
@Component
public class ContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * 容器加载完成后执行制定操作
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        LogUtil.info(getClass(), "ContextRefreshedEvent监听事件调用, Fatalis: Spring容器加载完成");

    }

}
