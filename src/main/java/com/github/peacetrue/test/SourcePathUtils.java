package com.github.peacetrue.test;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

/**
 * 源码路径工具类，用于获取源码的绝对路径。
 * 测试时，写出一个文件，需要文件的绝对路径，
 * 如果绝对路径直接写本机地址，那其他人就不适用了。
 *
 * @author peace
 */
public abstract class SourcePathUtils {

    public static final String PATH_SRC = File.separatorChar + "src";
    public static final String PATH_MAIN = File.separatorChar + "main";
    public static final String PATH_TEST = File.separatorChar + "test";
    public static final String PATH_JAVA = File.separatorChar + "java";
    public static final String PATH_RESOURCES = File.separatorChar + "resources";
    public static final String PATH_SRC_MAIN = PATH_SRC + PATH_MAIN;
    public static final String PATH_SRC_MAIN_JAVA = PATH_SRC_MAIN + PATH_JAVA;
    public static final String PATH_SRC_MAIN_RESOURCES = PATH_SRC_MAIN + PATH_RESOURCES;
    public static final String PATH_SRC_TEST = PATH_SRC + PATH_TEST;
    public static final String PATH_SRC_TEST_JAVA = PATH_SRC_TEST + PATH_JAVA;
    public static final String PATH_SRC_TEST_RESOURCES = PATH_SRC_TEST + PATH_RESOURCES;

    protected SourcePathUtils() {
    }

    /**
     * 获取项目绝对路径
     *
     * @return 项目绝对路径
     */
    public static String getProjectAbsolutePath() {
        return Objects.toString(System.getProperty("user.dir"), System.getenv("PWD"));
    }

    /**
     * 获取指定的绝对路径
     *
     * @param isTest      是否测试目录
     * @param isResources 是否资源目录
     * @param paths       附加的路径
     * @return 指定的绝对路径
     */
    public static String getCustomAbsolutePath(boolean isTest, boolean isResources, String... paths) {
        StringBuilder builder = new StringBuilder(getProjectAbsolutePath()).append(PATH_SRC)
                .append(isTest ? PATH_TEST : PATH_MAIN)
                .append(isResources ? PATH_RESOURCES : PATH_JAVA);
        Arrays.asList(paths).forEach(builder::append);
        return builder.toString();
    }

    /**
     * 获取测试资源的绝对路径
     *
     * @param paths 附加的路径
     * @return 指定的测试资源绝对路径
     */
    public static String getTestResourceAbsolutePath(String... paths) {
        return getCustomAbsolutePath(true, true, paths);
    }

}
