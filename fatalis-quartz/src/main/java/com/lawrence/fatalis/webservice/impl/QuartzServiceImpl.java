package com.lawrence.fatalis.webservice.impl;

import com.lawrence.fatalis.constant.ReloadConstant;
import com.lawrence.fatalis.webservice.QuartzService;
import org.springframework.stereotype.Component;

import javax.jws.WebService;

/**
 * cxf服务接口实现
 */
@WebService(serviceName = "QuartzService", targetNamespace = "http://fatalis.lawrence.com",
        endpointInterface = "com.lawrence.fatalis.webservice.QuartzService")
@Component
public class QuartzServiceImpl implements QuartzService {

    /**
     * 获取配置接口实现
     *
     * @param key
     * @return String
     */
    @Override
    public String getConfig(String key, String key2) {

        return ReloadConstant.getAutoProper().getString(key) + ReloadConstant.getAutoProper().getString(key2);
    }
}