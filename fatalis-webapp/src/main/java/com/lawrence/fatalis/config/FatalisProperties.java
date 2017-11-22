package com.lawrence.fatalis.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class FatalisProperties {

    @Value("${fatalis.redis-cluster-open}")
    private Boolean redisClusterOpen;
    @Value("${fatalis.multi-datasource-open}")
    private Boolean multiDatasourceOpen;
    @Value("${fatalis.swagger2-open}")
    private Boolean swagger2Open;
    @Value("${fatalis.uri-logging-open}")
    private Boolean uriLoggingOpen;

}
