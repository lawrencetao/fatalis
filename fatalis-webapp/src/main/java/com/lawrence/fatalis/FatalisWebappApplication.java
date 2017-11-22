package com.lawrence.fatalis;

import com.lawrence.fatalis.config.FatalisProperties;
import com.lawrence.fatalis.interceptor.URIInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;

@EnableTransactionManagement
@SpringBootApplication
public class FatalisWebappApplication extends WebMvcConfigurerAdapter {

	@Resource
	private FatalisProperties fatalisProperties;

	public static void main(String[] args) {
		SpringApplication.run(FatalisWebappApplication.class, args);
	}

	/**
	 * 添加资源处理handler
	 * 
	 * @param registry
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		// 设置static和templates目录的静态访问
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
		registry.addResourceHandler("/templates/**").addResourceLocations("classpath:/templates/");

		// 添加swagger2目录的静态访问
		if (fatalisProperties.getSwagger2Open()) {
			registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
			registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
		}

	}

	/**
	 * 添加拦截器interceptor
	 *
	 * @param registry
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		if (fatalisProperties.getUriLoggingOpen()) {

			/** addPathPatterns为拦截uri, excludePathPatterns为排除uri */

			// 添加uri拦截器, 打印访问uri地址
			registry.addInterceptor(new URIInterceptor()).addPathPatterns("/**");
			super.addInterceptors(registry);
		}

	}

}
