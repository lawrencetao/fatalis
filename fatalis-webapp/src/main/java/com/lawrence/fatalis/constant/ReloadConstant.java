package com.lawrence.fatalis.constant;


import lombok.Getter;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.ReloadingFileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
import java.math.BigDecimal;

/**
 * 自动重载配置
 */
public class ReloadConstant extends PropertiesConfiguration {

    private static File propertiesFile = new File("classpath:spring.properties");

    /** 取ReloadConstant对象, 根据active切换属性前缀, 不进行重载 */
    @Getter
    private static PropertiesConfiguration fixedProper = getAutoProper();

    /** 获取PropertiesConfiguration对象, 未添加active前缀, 不进行重载 */
    @Getter
    private static PropertiesConfiguration fixedOriProper = getAutoOriProper();


    /* 私有化构造器和属性 */
    private PropertiesConfiguration propertiesConfiguration;
    private String active;

    private ReloadConstant() {

    }
    private ReloadConstant(PropertiesConfiguration proper) {
        propertiesConfiguration = proper;
        active = proper.getString("active");
    }

    /**
     * 获取ReloadConstant对象, 根据active切换属性前缀, 自动重载
     *
     * @return PropertiesConfiguration
     */
    public static PropertiesConfiguration getAutoProper() {
        Parameters params = new Parameters();
        ReloadingFileBasedConfigurationBuilder<PropertiesConfiguration> builder =
                new ReloadingFileBasedConfigurationBuilder<PropertiesConfiguration>(PropertiesConfiguration.class)
                        .configure(params.fileBased().setFile(propertiesFile));

        PropertiesConfiguration proper = null;
        try {
            proper = new ReloadConstant(builder.getConfiguration());
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }

        return proper;
    }

    /**
     * 获取PropertiesConfiguration对象, 未添加active前缀, 自动重载
     *
     * @return PropertiesConfiguration
     */
    public static PropertiesConfiguration getAutoOriProper() {
        Parameters params = new Parameters();
        ReloadingFileBasedConfigurationBuilder<PropertiesConfiguration> builder =
                new ReloadingFileBasedConfigurationBuilder<PropertiesConfiguration>(PropertiesConfiguration.class)
                        .configure(params.fileBased().setFile(propertiesFile));

        PropertiesConfiguration proper = null;
        try {
            proper = builder.getConfiguration();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }

        return proper;
    }

    /**
     * 重写基本get方法, 加上properType环境标识
     *
     * @param key
     * @return
     */
    @Override
    public String getString(String key) {

        return propertiesConfiguration.getString(active + "." + key);
    }
    @Override
    public boolean getBoolean(String key) {

        return propertiesConfiguration.getBoolean(active + "." + key);
    }
    @Override
    public double getDouble(String key) {

        return propertiesConfiguration.getDouble(active + "." + key);
    }
    @Override
    public long getLong(String key) {

        return propertiesConfiguration.getLong(active + "." + key);
    }
    @Override
    public BigDecimal getBigDecimal(String key) {

        return propertiesConfiguration.getBigDecimal(active + "." + key);
    }

}
