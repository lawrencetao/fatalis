package com.lawrence.fatalis.config.druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.lawrence.fatalis.config.druid.properties.DruidMaster;
import com.lawrence.fatalis.config.druid.properties.DruidProperties;
import com.lawrence.fatalis.util.LogUtil;
import com.lawrence.fatalis.util.StringUtil;
import com.lawrence.fatalis.util.rsa7des.AESCoder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.sql.SQLException;

@Configuration
public class DruidConfig {
    public static final String MASTER = "master";
    public static final String SLAVE = "slave";

    @Resource
    private DruidProperties druidProperties;

    /**
     * 初始化druid数据源
     *
     * @param dataSource, type(master/slave), index(slave数据源配置集合的index)
     */
    public void initDatasource(DruidDataSource dataSource, String type, int index) {
        DruidMaster druidProper = null;

        // 设置主库/从库配置, 初始化数据源
        if (MASTER.equals(type)) {
            druidProper = druidProperties.getMaster();
        } else if (SLAVE.equals(type)) {
            druidProper = druidProperties.getSlave().get(index);
        }

        dataSource.setUrl(druidProper.getUrl());
        dataSource.setUsername(druidProper.getUsername());
        String password = druidProper.getPassword();

        if (StringUtil.isNotNull(password)) {

            // 配置文件中密码进行解密
            /*try {
                password = AESCoder.decrypt(password, AESCoder.CONFIG_KEY);
            } catch (Exception e) {
                e.printStackTrace();
            }*/

            dataSource.setPassword(password);
        }

        dataSource.setDriverClassName(druidProper.getDriverClassName());
        dataSource.setInitialSize(druidProper.getInitialSize());// 定义初始连接数
        dataSource.setMinIdle(druidProper.getMinIdle());// 最小空闲
        dataSource.setMaxActive(druidProper.getMaxActive());// 定义最大连接数
        dataSource.setMaxWait(druidProper.getMaxWait());// 最长等待时间

        // 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
        dataSource.setTimeBetweenEvictionRunsMillis(druidProper.getTimeBetweenEvictionRunsMillis());

        // 配置一个连接在池中最小生存的时间，单位是毫秒
        dataSource.setMinEvictableIdleTimeMillis(druidProper.getMinEvictableIdleTimeMillis());
        dataSource.setValidationQuery(druidProper.getValidationQuery());
        dataSource.setTestWhileIdle(druidProper.getTestWhileIdle());
        dataSource.setTestOnBorrow(druidProper.getTestOnBorrow());
        dataSource.setTestOnReturn(druidProper.getTestOnReturn());

        // 打开PSCache，并且指定每个连接上PSCache的大小
        dataSource.setPoolPreparedStatements(druidProper.getPoolPreparedStatements());
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(druidProper.getMaxPoolPreparedStatementPerConnectionSize());

        try {
            dataSource.setFilters(druidProper.getFilters());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        LogUtil.info(getClass(), "数据源 " + type + " 加载: " + dataSource.getUrl());

    }

    /**
     * Druid监控登陆配置
     *
     * @return ServletRegistrationBean
     */
    @Bean
    public ServletRegistrationBean DruidStatViewServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");

        // 监控登陆ip白名单/黑名单, 黑名单优先级高
        /*servletRegistrationBean.addInitParameter("allow", "127.0.0.1");
        servletRegistrationBean.addInitParameter("deny", "192.168.0.100");*/

        // 登录查看信息的账号密码.
        servletRegistrationBean.addInitParameter("loginUsername", "lawrence");
        servletRegistrationBean.addInitParameter("loginPassword", "lawrence");

        // 是否能够重置数据.
        servletRegistrationBean.addInitParameter("resetEnable", "false");

        return servletRegistrationBean;
    }

    /**
     * druid过滤器配置
     *
     * @return FilterRegistrationBean
     */
    @Bean
    public FilterRegistrationBean druidStatFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());

        // 过滤url-pattern
        filterRegistrationBean.addUrlPatterns("/*");

        // druid不过滤的资源
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");

        return filterRegistrationBean;
    }

}
