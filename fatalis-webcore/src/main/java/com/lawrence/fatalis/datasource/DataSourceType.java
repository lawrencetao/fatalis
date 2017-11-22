package com.lawrence.fatalis.datasource;

/**
 * 数据源类型枚举
 */
public enum DataSourceType {

	master("master", "主库: 写"),
	slave("slave", "从库: 读");

    private String type;
    private String name;

    DataSourceType(String type, String name) {
        this.type = type;
        this.name = name;
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    
}
