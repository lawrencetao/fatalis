package com.lawrence.fatalis.config.druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.lawrence.fatalis.util.StringUtil;
import com.lawrence.fatalis.util.rsa7des.AESCoder;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.sql.SQLException;

@Configuration
public class DruidConfig {

    @Resource
    private DruidProperties druidProperties;

    /**
     * 初始化druid数据源
     *
     * @param dataSource
     */
    public void initDatasource(DruidDataSource dataSource) {

        dataSource.setUrl(druidProperties.getUrl());
        dataSource.setUsername(druidProperties.getUsername());
        String password = druidProperties.getPassword();

        if (StringUtil.isNotNull(password)) {

            // 配置文件中密码进行解密
            /*try {
                password = AESCoder.decrypt(password, AESCoder.CONFIG_KEY);
            } catch (Exception e) {
                e.printStackTrace();
            }*/

            dataSource.setPassword(password);
        }

        dataSource.setDriverClassName(druidProperties.getDriverClassName());
        dataSource.setInitialSize(druidProperties.getInitialSize());// 定义初始连接数
        dataSource.setMinIdle(druidProperties.getMinIdle());// 最小空闲
        dataSource.setMaxActive(druidProperties.getMaxActive());// 定义最大连接数
        dataSource.setMaxWait(druidProperties.getMaxWait());// 最长等待时间

        // 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
        dataSource.setTimeBetweenEvictionRunsMillis(druidProperties.getTimeBetweenEvictionRunsMillis());

        // 配置一个连接在池中最小生存的时间，单位是毫秒
        dataSource.setMinEvictableIdleTimeMillis(druidProperties.getMinEvictableIdleTimeMillis());
        dataSource.setValidationQuery(druidProperties.getValidationQuery());
        dataSource.setTestWhileIdle(druidProperties.getTestWhileIdle());
        dataSource.setTestOnBorrow(druidProperties.getTestOnBorrow());
        dataSource.setTestOnReturn(druidProperties.getTestOnReturn());

        // 打开PSCache，并且指定每个连接上PSCache的大小
        dataSource.setPoolPreparedStatements(druidProperties.getPoolPreparedStatements());
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(druidProperties.getMaxPoolPreparedStatementPerConnectionSize());

        try {
            dataSource.setFilters(druidProperties.getFilters());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
