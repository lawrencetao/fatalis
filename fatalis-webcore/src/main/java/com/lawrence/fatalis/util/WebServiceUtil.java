package com.lawrence.fatalis.util;

import com.lawrence.fatalis.interceptor.ClientLoginInterceptor;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

/**
 * cxf通用调用webservice工具类
 */
public class WebServiceUtil {

    private static final String USERNAME = "";
    private static final String PASSWORD = "";

    /**
     * cxf方式调用webservice服务
     *
     * @param url, method, params
     * @return String
     */
    public static String cxfWebService(String url, String method, String[] auth, Object... params) {

        // 创建动态客户端
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient(url);

        // 需要验证登陆的账号: auth[0], 密码: auth[1]
        if (auth != null && auth.length >= 2) {
            client.getOutInterceptors().add(new ClientLoginInterceptor(auth[0], auth[1]));
        }

        Object[] objects;
        try {

            // 执行方法, 参数可依次传递
            objects = client.invoke(method, params);

            return (String) objects[0];
        } catch (java.lang.Exception e) {
            e.printStackTrace();

            return null;
        }
    }

}
