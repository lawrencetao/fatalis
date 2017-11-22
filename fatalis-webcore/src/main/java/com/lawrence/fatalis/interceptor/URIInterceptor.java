package com.lawrence.fatalis.interceptor;

import com.lawrence.fatalis.util.IPUtil;
import com.lawrence.fatalis.util.LogUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class URIInterceptor extends HandlerInterceptorAdapter {

    /**
     * 请求映射到controller之前进行处理
     *
     * @param request, response, handler
     * @param response
     * @param handler
     * @return boolean
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 打印当前请求的uri
        LogUtil.info(getClass(), "Ip: " + IPUtil.getClientIp(request) + ", 当前请求uri: " + request.getRequestURI());

        return true;
    }

    /**
     * 请求映射到controller完成之后, 视图解析之前, 进行处理
     *
     * @param request, response, handler, modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 请求映射到controller完成, 视图解析之后, 进行处理, 用于资源清理
     *
     * @param request, response, handler, ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {

    }

}
