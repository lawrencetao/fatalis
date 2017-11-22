package com.lawrence.fatalis.constant;

public class BaseConstant {

    /** 项目class的根目录 */
    public static final String ROOT_CLASS_PATH = BaseConstant.class.getResource("").getFile().replaceAll("(.*)classes(.*)", "$1classes");

}
