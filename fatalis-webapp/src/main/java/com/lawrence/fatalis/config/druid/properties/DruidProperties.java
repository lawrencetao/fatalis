package com.lawrence.fatalis.config.druid.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.List;

/**
 * druid配置实体
 */
@Configuration
@ConfigurationProperties(prefix = "fatalis.datasource")
@Data
public class DruidProperties {

    /** druid主从库配置 */
    private Class<? extends DataSource> type;
    private int readSize;
    private DruidMaster master;
    private List<DruidSlave> slave;

}

