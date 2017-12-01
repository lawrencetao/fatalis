package com.lawrence.fatalis.constant;

/**
 * 常量配置
 */
public class WebContant extends BaseConstant {

    /** 当前登陆用户shiro-session-key */
    public static final String SHIRO_SESSION_USER = "fatalis-webapp.shiro.currentUser";

    /** 数据有效标志状态 */
    public static final String STATUS_ON = "1";
    public static final String STATUS_OFF = "0";

    /** restful请求状态码 */
    public static final String SUCCESS = "200";// 处理成功
    public static final String SERVER_FAIL = "500";// 处理失败
    public static final String CLIENT_FAIL = "400";// 客户端参数错误
    public static final String SHIRO_FAIL = "800";// shiro权限异常
    public static final String SESSION_OUT = "999";// session超时

}
