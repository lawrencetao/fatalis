package com.lawrence.fatalis.config.shiro;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.ServletContainerSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Base64Utils;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    /**
     * 配置安全管理器
     *
     * @return DefaultWebSecurityManager
     */
    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        // 自定义realm对象
        securityManager.setRealm(myShiroRealm());

        // redis实现session管理
        securityManager.setSessionManager(sessionManager());

        // rememberMe管理器
        securityManager.setRememberMeManager(rememberMeManager());

        return securityManager;
    }

    /**
     * 用户认证realm
     *
     * @return MyShiroRealm
     */
    @Bean
    public MyShiroRealm myShiroRealm() {
        MyShiroRealm myShiroRealm = new MyShiroRealm();

        return myShiroRealm;
    }

    /**
     * servlet容器session管理器
     */
    @Bean
    public ServletContainerSessionManager sessionManager() {

        return new ServletContainerSessionManager();
    }

    /**
     * cookie管理对象rememberMe
     *
     * @return CookieRememberMeManager
     */
    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());

        // cookie加密的密钥, 生成方法见main方法
        cookieRememberMeManager.setCipherKey(Base64.decode("TGF3cmVuY2VUYW8ncyBmYQ=="));

        return cookieRememberMeManager;
    }

    /*
     * rememberMe的cookie对象
     *
     * @return SimpleCookie
     */
    private SimpleCookie rememberMeCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");

        // 生效时间7天, 单位秒
        simpleCookie.setMaxAge(3600 * 24 * 7);

        return simpleCookie;
    }

    /**
     * shiro生命周期处理器
     *
     * @return LifecycleBeanPostProcessor
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {

        return new LifecycleBeanPostProcessor();
    }


    /**
     * 启用shrio授权注解拦截方式，AOP式方法级权限检查
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor =
                new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);

        return authorizationAttributeSourceAdvisor;
    }

    /**
     * 配置shiro权限过滤器
     *
     * @param securityManager
     * @return ShiroFilterFactoryBean
     */
    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/templates/login.html");

        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/templates/index.html");

        // 未授权界面
        shiroFilterFactoryBean.setUnauthorizedUrl("/errorPage/403.html");

        // 通用权限列表
        Map<String, String> authMap = new LinkedHashMap<String, String>();

        authMap.put("/login", "anon");
        authMap.put("/logout", "anon");
        authMap.put("/kaptcha", "anon");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(authMap);

        return shiroFilterFactoryBean;
    }

    public static void main(String[] args) {
        String keyStr = "LawrenceTao's fatalis project";
        byte[] keyby;
        try {
            keyby = keyStr.getBytes("UTF-8");
            System.out.println("Cookie加密密钥: " + Base64Utils.encodeToString(Arrays.copyOf(keyby, 16)));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

}
