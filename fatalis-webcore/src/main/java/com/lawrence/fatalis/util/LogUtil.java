package com.lawrence.fatalis.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志工具类
 */
public class LogUtil {

    /**
     * 输出trace级别日志, 类名标题
     *
     * @param clz, message
     */
    public static void trace(Class clz, String message) {
        Logger log = LoggerFactory.getLogger(clz);
        log.trace("\n------------------------------------------------------------\n"
                + message +
                "\n------------------------------------------------------------");
    }

    /**
     * 输出trace级别日志, 自定义标题
     *
     * @param title, message
     */
    public static void trace(String title, String message) {
        Logger log = LoggerFactory.getLogger(title);
        log.trace("\n------------------------------------------------------------\n"
                + message +
                "\n------------------------------------------------------------");
    }

    /**
     * 输出trace级别日志, 类名标题, 声明异常
     *
     * @param clz, message, t
     */
    public static void trace(Class clz, String message, Throwable t) {
        Logger log = LoggerFactory.getLogger(clz);
        log.trace("\n------------------------------------------------------------\n"
                + message +
                "\n------------------------------------------------------------", t);
    }

    /**
     * 输出debug级别日志, 类名标题
     *
     * @param clz, message
     */
    public static void debug(Class clz, String message) {
        Logger log = LoggerFactory.getLogger(clz);
        log.debug("\n------------------------------------------------------------\n"
                + message +
                "\n------------------------------------------------------------");
    }

    /**
     * 输出debug级别日志, 自定义标题
     *
     * @param title, message
     */
    public static void debug(String title, String message) {
        Logger log = LoggerFactory.getLogger(title);
        log.debug("\n------------------------------------------------------------\n"
                + message +
                "\n------------------------------------------------------------");
    }

    /**
     * 输出debug级别日志, 类名标题, 声明异常
     *
     * @param clz, message, t
     */
    public static void debug(Class clz, String message, Throwable t) {
        Logger log = LoggerFactory.getLogger(clz);
        log.debug("\n------------------------------------------------------------\n"
                + message +
                "\n------------------------------------------------------------", t);
    }

    /**
     * 输出info级别日志, 类名标题
     *
     * @param clz, message
     */
    public static void info(Class clz, String message) {
        Logger log = LoggerFactory.getLogger(clz);
        log.info("\n------------------------------------------------------------\n"
                + message +
                "\n------------------------------------------------------------");
    }

    /**
     * 输出info级别日志, 自定义标题
     *
     * @param title, message
     */
    public static void info(String title, String message) {
        Logger log = LoggerFactory.getLogger(title);
        log.info("\n------------------------------------------------------------\n"
                + message +
                "\n------------------------------------------------------------");
    }

    /**
     * 输出info级别日志, 类名标题, 声明异常
     *
     * @param clz, message, t
     */
    public static void info(Class clz, String message, Throwable t) {
        Logger log = LoggerFactory.getLogger(clz);
        log.info("\n------------------------------------------------------------\n"
                + message +
                "\n------------------------------------------------------------", t);
    }

    /**
     * 输出warn级别日志, 类名标题
     *
     * @param clz, message
     */
    public static void warn(Class clz, String message) {
        Logger log = LoggerFactory.getLogger(clz);
        log.warn("\n------------------------------------------------------------\n"
                + message +
                "\n------------------------------------------------------------");
    }

    /**
     * 输出warn级别日志, 自定义标题
     *
     * @param title, message
     */
    public static void warn(String title, String message) {
        Logger log = LoggerFactory.getLogger(title);
        log.warn("\n------------------------------------------------------------\n"
                + message +
                "\n------------------------------------------------------------");
    }

    /**
     * 输出warn级别日志, 类名标题, 声明异常
     *
     * @param clz, message, t
     */
    public static void warn(Class clz, String message, Throwable t) {
        Logger log = LoggerFactory.getLogger(clz);
        log.warn("\n------------------------------------------------------------\n"
                + message +
                "\n------------------------------------------------------------", t);
    }

    /**
     * 输出error级别日志, 类名标题
     *
     * @param clz, message
     */
    public static void error(Class clz, String message) {
        Logger log = LoggerFactory.getLogger(clz);
        log.error("\n------------------------------------------------------------\n"
                + message +
                "\n------------------------------------------------------------");
    }

    /**
     * 输出error级别日志, 自定义标题
     *
     * @param title, message
     */
    public static void error(String title, String message) {
        Logger log = LoggerFactory.getLogger(title);
        log.error("\n------------------------------------------------------------\n"
                + message +
                "\n------------------------------------------------------------");
    }

    /**
     * 输出error级别日志, 类名标题, 声明异常
     *
     * @param clz, message, t
     */
    public static void error(Class clz, String message, Throwable t) {
        Logger log = LoggerFactory.getLogger(clz);
        log.error("\n------------------------------------------------------------\n"
                + message +
                "\n------------------------------------------------------------", t);
    }

}
