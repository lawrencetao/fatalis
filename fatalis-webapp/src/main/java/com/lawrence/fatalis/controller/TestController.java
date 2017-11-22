package com.lawrence.fatalis.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lawrence.fatalis.base.BaseController;
import com.lawrence.fatalis.constant.ReloadConstant;
import com.lawrence.fatalis.model.Commission;
import com.lawrence.fatalis.redis.RedisOperator;
import com.lawrence.fatalis.service.CommissionService;
import com.lawrence.fatalis.test.TestObj;
import com.lawrence.fatalis.util.DateUtil;
import com.lawrence.fatalis.util.LogUtil;
import com.lawrence.fatalis.util.StringUtil;
import com.lawrence.fatalis.util.rsa7des.AESCoder;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/test")
public class TestController extends BaseController {

    @Resource
    private CommissionService testService;

    /**
     * 测试修改配置自动加载
     */
    @ApiOperation(value = "测试修改配置自动加载", notes = "测试修改配置自动加载")
    @RequestMapping(value = "/config", method = {RequestMethod.GET, RequestMethod.POST})
    public void config(HttpServletRequest request, HttpServletResponse response) {

        long l1 = System.nanoTime();

        String conf = "";
        String conf1 = "";
        String conf2 = "";
        String conf3 = "";
        try {
            conf = ReloadConstant.getAutoProper().getString("contextUrl");
            conf1 = ReloadConstant.getAutoOriProper().getString("spring.jndi.ignore");
            conf2 = ReloadConstant.getFixedProper().getString("contextUrl");
            conf3 = ReloadConstant.getFixedOriProper().getString("spring.jndi.ignore");
        } catch (Exception e) {
            e.printStackTrace();
        }

        long l2 = System.nanoTime();
        System.out.println("div: " + (l2 - l1));

        JSONObject json = new JSONObject();
        json.put("config", conf);
        json.put("config1", conf1);
        json.put("config2", conf2);
        json.put("config3", conf3);

        LogUtil.info(getClass(), json.toString());

        responseWrite(response, json.toString(), null);
    }

    /**
     * 测试controller层json返回页面
     */
    @ApiOperation(value = "测试controller层json返回页面", notes = "测试controller层json返回页面")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", dataType = "String", required = false, value = "对象id", defaultValue = "test")
    })
    @ApiResponses({
            @ApiResponse(code = 404, message = "请求地址错误")
    })
    @RequestMapping(value = "/json", method = {RequestMethod.GET, RequestMethod.POST})
    public JSONObject json(HttpServletRequest request, String id) {
        TestObj to = new TestObj();
        to.setId(id);
        to.setArray(new JSONArray());
        to.setJson(new JSONObject());

        LogUtil.info(getClass(), JSON.toJSONString(to));

        return pubResponseJson(true, "成功", to);
    }

    /**
     * 测试StringUtil和DateUtil
     */
    @ApiOperation(value = "测试StringUtil和DateUtil", notes = "测试StringUtil和DateUtil")
    @RequestMapping(value = "/dateStr", method = {RequestMethod.GET, RequestMethod.POST})
    public JSONObject dateStr(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        Date date = new Date();
        json.put("年: ", DateUtil.getYear(date));
        json.put("月: ", DateUtil.getMonth(date));
        json.put("日: ", DateUtil.getDay(date));
        json.put("年龄: ", DateUtil.getAge("19880902", "yyyyMMdd"));
        json.put("迟月: ", DateUtil.getMonthLaterDateString("19880902", "yyyyMMdd", 2));
        json.put("null空: ", StringUtil.isNull(null));
        json.put("''空: ", StringUtil.isNull(" "));
        json.put("'null'空: ", StringUtil.isNull("null"));

        return json;
    }

    @Resource
    private RedisOperator redisOperator;
    /*@Resource
    private ClusterOperator clusterOperator;*/

    /**
     * 测试redis和集群cluster
     *
     * @param type, param
     * @return JSONObject
     */
    @RequestMapping(value = "/redis", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiIgnore
    public JSONObject redis(String type, String param) {
        TestObj to = new TestObj();
        to.setId(param);

        if ("set".equals(type)) {
            redisOperator.setObject("test", to);
        } else if ("get".equals(type)) {
            to = redisOperator.getObject("test");
            to.setList(new ArrayList<>());
        } else {

            return new JSONObject();
        }

        JSONObject json = new JSONObject();
        json.put("test", to);

        return json;
    }

    @Resource
    private CommissionService commissionService;

    /**
     * 测试多数据源读写分离, 主库写
     *
     * @param request
     * @return JSONObject
     */
    @RequestMapping(value = "/add", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiIgnore
    public JSONObject add(HttpServletRequest request) {
        String sid = request.getParameter("sid");
        String config = request.getParameter("config");

        Commission commission = new Commission();
        if (StringUtil.isNull(sid)) {
            sid = StringUtil.getRandomNum(10);
        }
        if (StringUtil.isNull(config)) {
            config = StringUtil.get64RandomStr();
        }
        commission.setSid(sid);
        commission.setConfig(config);
        commissionService.addCommission(commission);


        JSONObject json = new JSONObject();
        json.put("commission", commission);

        return json;
    }

    /**
     * 测试多数据源读写分离, 从库读
     *
     * @param request
     * @return JSONObject
     */
    @RequestMapping(value = "/query", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiIgnore
    public JSONObject query(HttpServletRequest request) {
        String sid = request.getParameter("sid");
        String config = request.getParameter("config");

        List<Commission> list = commissionService.queryCommission();

        JSONObject json = new JSONObject();
        json.put("size", list.size());
        json.put("list", list);

        return json;
    }

    /**
     * 测试多数据源读写分离, 主库读
     *
     * @param request
     * @return JSONObject
     */
    @RequestMapping(value = "/get", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiIgnore
    public JSONObject get(HttpServletRequest request) {
        String sid = request.getParameter("sid");

        long l1 = System.nanoTime();

        Commission com = commissionService.getCommission(sid);

        JSONObject json = new JSONObject();
        json.put("com", com);

        long l2 = System.nanoTime();

        System.out.println(l2 - l1);

        return json;
    }

    /**
     * 测试session共享
     *
     * @param request
     * @return JSONObject
     */
    @RequestMapping(value = "/session", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiIgnore
    public JSONObject session(HttpServletRequest request) {
        HttpSession session = request.getSession();

        String uid = (String) session.getAttribute("uid");
        if (StringUtil.isNull(uid)) {
            uid = StringUtil.getUUIDStr();
        }

        session.setAttribute("uid", uid);

        JSONObject json = new JSONObject();
        json.put("uid", uid);

        return json;
    }

    public static void main(String[] args) {
        try {
            String cron = "0/10 * * * * ?";
            System.out.println(cron = AESCoder.encrypt(cron, AESCoder.URLPARAM_KEY));
            System.out.println(AESCoder.decrypt(cron, AESCoder.URLPARAM_KEY));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
