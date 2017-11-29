package com.lawrence.fatalis.config.cxf;

import com.lawrence.fatalis.webservice.QuartzService;
import org.apache.cxf.bus.spring.BusDefinitionParser;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.feature.FastInfosetFeature;
import org.apache.cxf.feature.Feature;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import javax.annotation.Resource;
import javax.xml.ws.Endpoint;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class CxfConfig {

    @Resource
    private QuartzService quartzService;

    /**
     * 注册cxf过滤器
     *
     * @return ServletRegistrationBean
     */
    @Bean
    public ServletRegistrationBean cxfServlet(){
        ServletRegistrationBean bean = new ServletRegistrationBean(new CXFServlet(), "/services/*");
        bean.setLoadOnStartup(0);
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);

        return bean;
    }

    /**
     * 配置springBus
     *
     * @return SpringBus
     */
    @Bean
    public SpringBus cxf() {
        SpringBus bus = new SpringBus();
        BusDefinitionParser.BusConfig busConfig = new BusDefinitionParser.BusConfig("serverBus");
        FastInfosetFeature feature = new FastInfosetFeature();
        feature.setForce(false);
        List<Feature> features = new ArrayList<>(Arrays.asList(feature));
        busConfig.setFeatures(features);
        bus.setBusConfig(busConfig);

        return bus;
    }

    /**
     * 配置服务发布地址
     *
     * @return Endpoint
     */
    @Bean
    public Endpoint endpoint(SpringBus springBus) {
        EndpointImpl endpoint = new EndpointImpl(springBus, quartzService);
        endpoint.publish("/QuartzService");

        // 通过拦截器校验用户名与密码
        /*endpoint.getInInterceptors().add(new AuthInterceptor());*/

        return endpoint;
    }

}