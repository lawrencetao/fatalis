package com.lawrence.fatalis.aop;

import com.lawrence.fatalis.base.BaseController;
import com.lawrence.fatalis.constant.ReloadConstant;
import com.lawrence.fatalis.constant.WebContant;
import com.lawrence.fatalis.util.HttpHelper;
import com.lawrence.fatalis.util.LogUtil;
import com.lawrence.fatalis.util.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 验证登陆aop类
 */
@Aspect
@Component
public class SessionValidateAop extends BaseController {

    /**
     * 对所有controller请求进行shiro-session校验
     */
    @Pointcut("execution(* com.lawrence.fatalis.controller..*.*(..))")
    public void inSession() {}

    @Around("inSession()")
    public Object sessionValidate(ProceedingJoinPoint point) throws Throwable {
        Subject subject = SecurityUtils.getSubject();
        String servletPath = HttpHelper.getRequest().getServletPath();

        String passUri = ReloadConstant.getAutoOriProper().getString("accessUris");
        String[] patterns = StringUtil.removeBlank(passUri).split(",");

        // 允许直接访问的uri, 不做拦截
        for (String str : patterns) {
            if (servletPath.startsWith(str)) {

                return point.proceed();
            }
        }

        // 拦截的uri, 进行session超时校验
        String username = (String) subject.getSession().getAttribute(WebContant.SHIRO_SESSION_USER);
        if(StringUtil.isNull(username)){
            subject.logout();

            LogUtil.info(getClass(), "Session超时或已退出登录");

            return pubResponseJson(WebContant.SESSION_OUT, "Session超时或已退出登录", null);
        }else{

            return point.proceed();
        }
    }

}
