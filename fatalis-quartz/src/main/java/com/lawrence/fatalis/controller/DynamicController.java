package com.lawrence.fatalis.controller;

import com.alibaba.fastjson.JSONObject;
import com.lawrence.fatalis.base.SpringContext;
import com.lawrence.fatalis.constant.ReloadConstant;
import com.lawrence.fatalis.util.StringUtil;
import com.lawrence.fatalis.util.encrypt.AESCoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;

/**
 * 动态更新触发器cron表达式
 */
@RestController
@RequestMapping("/schedule")
public class DynamicController {

    /**
     * 更新动态任务时间
     *
     * @param taskBeanName(定时任务实例名), encryptCron(新cron表达式的加密字符串)
     * @return JSONObject
     */
    @RequestMapping("/updateCron/{taskBeanName}/{encryptCron}")
    @SuppressWarnings("unchecked")
    public JSONObject updateDynamicScheduledTask(@PathVariable String taskBeanName, @PathVariable String encryptCron) {
        JSONObject json = new JSONObject();

        if (StringUtil.isNull(encryptCron)) {
            json.put("result", "表达式cron不能设置为空");
        } else {

            // 判断是否cron更新是否被允许
            if (ReloadConstant.getAutoProper().getBoolean("enableUpdate")) {
                try {
                    String decryptCron = AESCoder.decrypt(encryptCron, AESCoder.URLPARAM_KEY);

                    Object obj = SpringContext.getBean(taskBeanName);
                    Class clz = obj.getClass();
                    Method method = clz.getMethod("setCron", String.class);
                    method.invoke(obj, decryptCron);

                    json.put("result", "表达式cron已更新: " + decryptCron);
                } catch (Exception e) {
                    e.printStackTrace();
                    json.put("result", "实例名taskBeanName不存在或表达式cron格式错误");
                }
            } else {
                json.put("result", "表达式cron更新操作被禁止");
            }
        }

        return json;
    }

}
