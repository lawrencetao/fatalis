package com.lawrence.fatalis.config;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * 错误页配置
 */
@Configuration
public class ErrorPageConfig {

    /**
     * 拦截控制层401, 404, 500错误, 跳转对应页面
     *
     * @return EmbeddedServletContainerCustomizer
     */
    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {

        return container -> {
            ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/errorPage/401.html");
            ErrorPage error403Page = new ErrorPage(HttpStatus.FORBIDDEN, "/errorPage/403.html");
            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/errorPage/404.html");
            ErrorPage error405Page = new ErrorPage(HttpStatus.NOT_FOUND, "/errorPage/405.html");
            container.addErrorPages(error401Page, error403Page, error404Page, error405Page);
        };
    }
}
