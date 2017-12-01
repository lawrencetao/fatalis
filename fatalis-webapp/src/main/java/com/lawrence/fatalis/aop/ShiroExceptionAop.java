package com.lawrence.fatalis.aop;

import com.alibaba.fastjson.JSONObject;
import com.lawrence.fatalis.base.BaseController;
import com.lawrence.fatalis.constant.WebContant;
import com.lawrence.fatalis.util.StringUtil;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.UnknownSessionException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * shiro异常处理aop
 * 拦截用户权限异常, session超时异常等, 返回ajax通用json
 */
@ControllerAdvice
public class ShiroExceptionAop extends BaseController {

    /**
     * 拦截shiro无权限异常
     */
    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public JSONObject noauth(AuthorizationException e) {
        e.printStackTrace();

        return pubResponseJson(WebContant.SHIRO_FAIL, "登陆用户无权限", null);
    }

    /**
     * 拦截session超时异常
     */
    @ExceptionHandler(InvalidSessionException.class)
    @ResponseStatus(HttpStatus.OK)
    public JSONObject timeout(InvalidSessionException e, HttpServletRequest request, HttpServletResponse response) {
        e.printStackTrace();

        // session超时, ajax响应设置session状态
        if (StringUtil.isNotNull(request.getHeader("x-requested-with")) &&
                request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
            response.setHeader("sessionstatus", "timeout");
        }

        return pubResponseJson(WebContant.SESSION_OUT, "Session超时, 请重新登录", null);
    }

    /**
     * 拦截session未知异常
     */
    @ExceptionHandler(UnknownSessionException.class)
    @ResponseStatus(HttpStatus.OK)
    public JSONObject sessionTimeout(UnknownSessionException e) {
        e.printStackTrace();

        return pubResponseJson(WebContant.SESSION_OUT, "Session未知, 请重新登录", null);
    }

}
