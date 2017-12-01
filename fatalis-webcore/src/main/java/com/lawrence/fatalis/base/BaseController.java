package com.lawrence.fatalis.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.lawrence.fatalis.util.StringUtil;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class BaseController {

    /**
     * 通用组装json, 返回页面
     *
     * @param code, message, data
     * @return JSONObject
     */
    public JSONObject pubResponseJson(String code, String message, Object data) {
        if (StringUtil.isNull(code)) {
            throw new RuntimeException("返回状态码不能为空");
        }

        JSONObject json = new JSONObject();

        json.put("success", code);
        json.put("message", message == null ? "" : message);

        // 格式化json, null值不输出
        data = JSON.toJSONString(data, SerializerFeature.PrettyFormat);

        json.put("data", data == null ? "" : JSON.parseObject((String) data));

        return json;
    }

    /**
     * 通过response回写str
     *
     * @param response, str, contentType(默认application/json;charset=UTF-8)
     */
    public void responseWrite(HttpServletResponse response, String str, String contentType) {
        if (StringUtil.isNull(contentType)) {
            contentType = MediaType.APPLICATION_JSON_UTF8_VALUE;
        }
        response.setContentType(contentType);

        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(str);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.flush();
            writer.close();
        }
    }

}
