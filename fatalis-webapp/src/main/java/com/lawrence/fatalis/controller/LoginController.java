package com.lawrence.fatalis.controller;

import com.alibaba.fastjson.JSONObject;
import com.lawrence.fatalis.base.BaseController;
import com.lawrence.fatalis.constant.WebContant;
import com.lawrence.fatalis.util.LogUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/business")
public class LoginController extends BaseController {

    /**
     * 登录请求
     *
     * @param username, password
     * @return JSONObject
     */
    @RequestMapping(value = "/login")
    public JSONObject login(String username, String password, Boolean rememberMe) {
        JSONObject res = new JSONObject();
        try {
            UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
            SecurityUtils.getSubject().login(token);
            res.put("status", 200);
            res.put("message", "登录成功");

            SecurityUtils.getSubject().getSession().setAttribute(WebContant.SHIRO_SESSION_USER, username);
        } catch (Exception e) {
            res.put("status", 500);
            res.put("message", e.getMessage());
        }

        return res;
    }

    /**
     * 退出登陆
     *
     * @return JSONObject
     */
    @RequestMapping(value = "/logout")
    public JSONObject logout() {
        try {
            SecurityUtils.getSubject().logout();

            SecurityUtils.getSubject().getSession().setAttribute(WebContant.SHIRO_SESSION_USER, null);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return pubResponseJson(WebContant.SUCCESS, "退出登陆成功", null);
    }

    /**
     * 测试shiro对业务1的操作, 用户需要登陆, 有权限/business/busi1
     *
     * @return JSONObject
     */
    @RequiresPermissions("/business/busi1")
    @RequestMapping(value = "/dobusi1")
    public JSONObject busi1() {

        LogUtil.info(getClass(), "业务操作1, 执行完成");

        return pubResponseJson(WebContant.SUCCESS, "busi1完成", null);
    }

    /**
     * 测试shiro对业务2的操作, 用户需要登陆, 不限定权限
     *
     * @return JSONObject
     */
    @RequestMapping(value = "/dobusi2")
    public JSONObject busi2() {

        LogUtil.info(getClass(), "业务操作2, 执行完成");

        return pubResponseJson(WebContant.SUCCESS, "busi2完成", null);
    }

    /**
     * 测试shiro对业务3的操作, 用户需要登陆, 但没有权限/business/busi3
     *
     * @return JSONObject
     */
    @RequiresPermissions("/business/busi3")
    @RequestMapping(value = "/dobusi3")
    public JSONObject busi3() {

        LogUtil.info(getClass(), "业务操作3, 执行完成");

        return pubResponseJson(WebContant.SUCCESS, "busi3完成", null);
    }

}
