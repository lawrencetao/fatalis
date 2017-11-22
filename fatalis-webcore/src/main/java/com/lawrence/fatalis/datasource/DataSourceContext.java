package com.lawrence.fatalis.datasource;

/**
 * 本地线程，数据源上下文
 */
public class DataSourceContext {

	private static final ThreadLocal<String> local = new ThreadLocal<>();

    public static ThreadLocal<String> getLocal() {

        return local;
    }

    /**
     * 主库type
     */
    public static void setMaster() {
        local.set(DataSourceType.master.getType());
    }

    /**
     * 从库type
     */
    public static void setSlave() {
        local.set(DataSourceType.slave.getType());
    }

    public static String getJdbcType() {

        return local.get();
    }
    
    public static void clear(){
    	local.remove();
    }

}
