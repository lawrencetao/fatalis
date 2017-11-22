package com.lawrence.fatalis.config.session;

import org.springframework.context.annotation.Bean;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieHttpSessionStrategy;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;
  
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 1800)
public class SpringSessionConfig {

    /**
     * 使用cookie确定spring-session信息
     *
     * @return HttpSessionStrategy
     */
    @Bean
    public HttpSessionStrategy httpSessionStrategy() {

        return new CookieHttpSessionStrategy();
    }

    /**
     * 使用header确定spring-session信息
     *
     * @return HttpSessionStrategy
     */
    /*@Bean
    public HttpSessionStrategy httpSessionStrategy() {

        return new HeaderHttpSessionStrategy();
    }*/

    /**
     * 设置redis不再执行config命令
     *
     * @return ConfigureRedisAction
     */
    @Bean
    public static ConfigureRedisAction configureRedisAction() {

        return ConfigureRedisAction.NO_OP;
    }

} 