package com.lawrence.fatalis.config.mybatis;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import com.lawrence.fatalis.config.FatalisProperties;
import com.lawrence.fatalis.config.druid.DruidConfig;
import com.lawrence.fatalis.config.druid.properties.DruidProperties;
import com.lawrence.fatalis.config.druid.properties.DruidSlave;
import com.lawrence.fatalis.datasource.DataSourceContext;
import com.lawrence.fatalis.datasource.DataSourceType;
import com.lawrence.fatalis.util.LogUtil;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * mybatis-plus配置
 */
@Configuration
@EnableTransactionManagement(order = 2)// 让spring事务数据源切换aop的后加载
@MapperScan(basePackages = "com.lawrence.fatalis.dao")
public class MybatisPlusConfig {

    @Resource
    private MybatisProperties mybatisProperties;
    @Resource
    private DruidConfig druidConfig;
    @Resource
    private DruidProperties druidProperties;
    @Resource
    private FatalisProperties fatalisProperties;

    /**
     * sessionFactory配置
     *
     * @return GlobalConfiguration
     */
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(switchDataSourceProxy());// 数据源
        Interceptor[] interceptor = {new PaginationInterceptor()};
        sqlSessionFactory.setPlugins(interceptor);// 分页插件
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactory.setMapperLocations(resolver.getResources(mybatisProperties.getMapperLocations()));
        sqlSessionFactory.setConfigLocation(resolver.getResource(mybatisProperties.getConfigLocation()));
        sqlSessionFactory.setTypeAliasesPackage(mybatisProperties.getTypeAliasesPackage());

        return sqlSessionFactory.getObject();
    }

    /**
     * 根据读写分离开关, 返回不同AbstractRoutingDataSource对象给mybatis
     *
     * @return DataSource
     */
    private DataSource switchDataSourceProxy() {
        DataSource dataSourceProcy = null;

        // 读写分离开关关闭
        if (!fatalisProperties.getMultiDatasourceOpen()) {
            dataSourceProcy = singleDataSourceProxy();
        } else {
            dataSourceProcy = multiDataSourceProxy();
        }

        return dataSourceProcy;
    }

    /**
     * 读写分离开关关闭时, 加载数据源代理类
     *
     * @return DruidDataSource
     */
    @Bean(name = "singleDataSourceProxy")
    @ConditionalOnProperty(prefix = "fatalis", name = "multi-datasource-open", havingValue = "false")
    public DruidDataSource singleDataSourceProxy() {

        // 单数据源, master
        DruidDataSource dataSource = new DruidDataSource();
        druidConfig.initDatasource(dataSource, DruidConfig.MASTER, 0);

        return dataSource;
    }

    /**
     * 读写分离开关打开时, 加载数据源代理类
     *
     * @return AbstractRoutingDataSource
     */
    @Bean(name = "multiDataSourceProxy")
    @ConditionalOnProperty(prefix = "fatalis", name = "multi-datasource-open", havingValue = "true")
    public AbstractRoutingDataSource multiDataSourceProxy() {

        // 多数据源, master
        DruidDataSource masterDataSource = new DruidDataSource();
        druidConfig.initDatasource(masterDataSource, DruidConfig.MASTER, 0);

        // 多数据源, slave, 若干
        List<DruidSlave> slaveList = druidProperties.getSlave();

        // 从库数据源数量
        final int readSize = slaveList.size();

        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceType.master.getType(), masterDataSource);
        for (int i = 0; i < slaveList.size(); i++) {
            DruidDataSource slaveDataSource = new DruidDataSource();
            druidConfig.initDatasource(slaveDataSource, DruidConfig.SLAVE, i);

            try {
                slaveDataSource.init();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            targetDataSources.put(DataSourceType.slave.getType() + i, slaveDataSource);
        }

        /** 代理类, 返回对应type数据源 */
        AbstractRoutingDataSource proxy = new AbstractRoutingDataSource() {
            private AtomicLong count = new AtomicLong(0);

            /** 重写determineCurrentLookupKey方法, 获取数据源key */
            @Override
            protected Object determineCurrentLookupKey() {
                String typeKey = DataSourceContext.getJdbcType();

                if (typeKey == null) {

                    return DataSourceType.master.getType();
                }

                if (typeKey.equals(DataSourceType.master.getType())) {

                    LogUtil.info(getClass(), "切换到数据源master: 写操作");

                    return DataSourceType.master.getType();
                }

                // 读库负载均衡
                long number = count.getAndIncrement();
                long lookupIndex = number % readSize;
                lookupIndex = Math.abs(lookupIndex);

                LogUtil.info(getClass(), "切换到数据源slave" + lookupIndex + ": 读操作");

                return DataSourceType.slave.getType() + lookupIndex;
            }
        };

        proxy.setDefaultTargetDataSource(masterDataSource);//默认库
        proxy.setTargetDataSources(targetDataSources);

        return proxy;
    }

    /**
     * SqlSessionTemplate配置
     *
     * @return SqlSessionTemplate
     */
    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {

        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     * 事务管理
     *
     * @return DataSourceTransactionManager
     */
    @Bean(name = "transactionManager")
    public DataSourceTransactionManager transactionManager() {

        return new DataSourceTransactionManager(switchDataSourceProxy());
    }

}

