package com.lawrence.fatalis.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

/**
 * cxf服务接口
 */
@WebService(name = "QuartzService", targetNamespace = "http://fatalis.lawrence.com")
public interface QuartzService {

    /**
     * 获取配置接口
     *
     * @param key
     * @return String
     */
    @WebMethod
    @WebResult(name = "res")
    public String getConfig(@WebParam String key, @WebParam String key2);

}