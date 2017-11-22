package com.lawrence.fatalis.config.swagger;

import io.swagger.annotations.ApiOperation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger配置类
 */
@Configuration
@ConditionalOnProperty(prefix = "fatalis", name = "swagger2-open", havingValue = "true")
@EnableSwagger2
public class SwaggerConfig {

    /**
     * 创建restful接口api文档配置
     *
     * @return Docket
     */
    @Bean
    public Docket createRestApi() {

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                /*.apis(RequestHandlerSelectors.basePackage("com.lawrence.fatalis.controller"))*/
                .paths(PathSelectors.any())
                .build();
    }
    private ApiInfo apiInfo() {

        return new ApiInfoBuilder()
                .title("Swagger2创建api文档")
                .description("简单优雅的restful风格, http://fatalis.lawrence.com")
                .termsOfServiceUrl("http://fatalis.lawrence.com")
                .version("1.0.0")
                .build();
    }

}
