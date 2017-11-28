package com.lawrence.fatalis.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
import lombok.Getter;
import org.springframework.http.*;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.*;

/**
 * restful请求工具类
 */
public enum RestHttpUtil {
    INSTANCE;

    @Getter
    private RestTemplate restTemplate = initRest();

    /* 初始化单例 */
    private RestTemplate initRest() {
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        messageConverters.add(new FormHttpMessageConverter());

        // 设置httpMessageConverter
        FastJsonHttpMessageConverter4 fastJsonHttpMessageConverter4 = new FastJsonHttpMessageConverter4();

        // 设置fastJsonConfig
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        fastJsonConfig.setCharset(Charset.forName("UTF-8"));
        fastJsonHttpMessageConverter4.setFastJsonConfig(fastJsonConfig);

        // 设置mediaTypes
        ArrayList<MediaType> mediaTypes = new ArrayList<>(Arrays.asList(MediaType.APPLICATION_JSON_UTF8,
                MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_XHTML_XML));
        fastJsonHttpMessageConverter4.setSupportedMediaTypes(mediaTypes);

        messageConverters.add(fastJsonHttpMessageConverter4);
        RestTemplate restTemplate = new RestTemplate(messageConverters);

        return restTemplate;
    }

    /**
     * 发送post请求
     *
     * @param uri, veriable(uri参数中{}变量替换成对应key的value), headers, param, contentType
     * @return String
     */
    @SuppressWarnings("unchecked")
    public String restPost(String uri, Map<String, String> uriVeriables, Map<String, String> headers, Object param, String contentType) {

        // 校验uri非空
        checkURI(uri);

        // 设置requestHeaders
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-type", StringUtil.isNull(contentType) ? "application/x-www-form-urlencoded;charset=UTF-8" : contentType);
        if (headers != null && headers.size() > 0) {
            Set<Map.Entry<String, String>> set = headers.entrySet();
            for (Map.Entry<String, String> entry : set) {
                httpHeaders.add(entry.getKey(), entry.getValue());
            }
        }

        // 设置requestEntity
        HttpEntity httpEntity = null;
        if (param instanceof String) {
            httpEntity = new HttpEntity<String>((String) param, httpHeaders);
        } else if (param instanceof LinkedMultiValueMap) {
            httpEntity = new HttpEntity<LinkedMultiValueMap<String, Object>>((LinkedMultiValueMap) param, httpHeaders);
        } else if (param instanceof JSONObject) {
            httpEntity = new HttpEntity<JSONObject>((JSONObject) param, httpHeaders);
        } else if (param != null) {
            throw new RuntimeException("Param参数必须为String, JSONObject或LinkedMultiValueMap类型");
        }

        RestTemplate temp = getRestTemplate();
        String res;

        // uriVeriables不为空时, 替换uri中的参数
        if (uriVeriables != null && uriVeriables.size() > 0) {
            res = temp.postForObject(uri, httpEntity, String.class, uriVeriables);
        } else {
            res = temp.postForObject(uri, httpEntity, String.class);
        }

        return res;
    }

    /**
     * 发送get请求
     *
     * @return String
     */
    public String restGet(String uri, Map<String, String> uriVeriables) {

        // 校验uri非空
        checkURI(uri);

        RestTemplate temp = getRestTemplate();
        String res;

        // uriVeriables不为空时, 替换uri中的参数
        if (uriVeriables != null && uriVeriables.size() > 0) {
            res = temp.getForObject(uri, String.class, uriVeriables);
        } else {
            res = temp.getForObject(uri, String.class);
        }

        return res;
    }

    /**
     * 发送get请求, 带请求头
     *
     * @return String
     */
    public String restGet(String uri, Map<String, String> uriVeriables, Map<String, String> headers, String contentType) {

        // 校验uri非空
        checkURI(uri);

        // 设置requestHeaders
        HttpHeaders httpHeaders = new HttpHeaders();;
        httpHeaders.set("Content-type", StringUtil.isNull(contentType) ? "application/x-www-form-urlencoded;charset=UTF-8" : contentType);
        if (headers != null && headers.size() > 0) {
            Set<Map.Entry<String, String>> set = headers.entrySet();
            for (Map.Entry<String, String> entry : set) {
                httpHeaders.add(entry.getKey(), entry.getValue());
            }
        }

        // 设置requestEntity
        HttpEntity<String> requestEntity = new HttpEntity<String>(null, httpHeaders);

        RestTemplate temp = getRestTemplate();

        ResponseEntity<String> response = null;

        // uriVeriables不为空时, 替换uri中的参数
        if (uriVeriables != null && uriVeriables.size() > 0) {
            response = temp.exchange(uri, HttpMethod.GET, requestEntity, String.class, uriVeriables);
        } else {
            response = temp.exchange(uri, HttpMethod.GET, requestEntity, String.class);
        }

        return response.getBody();
    }

    /* 校验请求uri */
    private void checkURI(String uri) {
        if (StringUtil.isNull(uri)) {
            throw new RuntimeException("Rest请求uri不能为空");
        }
    }

}
