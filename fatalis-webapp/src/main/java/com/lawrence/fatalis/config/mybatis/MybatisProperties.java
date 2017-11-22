package com.lawrence.fatalis.config.mybatis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "mybatis-plus")
@Data
public class MybatisProperties {

    private String mapperLocations;
    private String typeAliasesPackage;
    private String mapperScanPackage;
    private String configLocation;

}
