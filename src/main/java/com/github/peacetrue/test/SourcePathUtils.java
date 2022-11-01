package com.github.peacetrue.test;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

/**
 * 源码路径工具类，用于获取源码的绝对路径。
 * <p>
 * 测试时，向项目内文档目录下输出内容，需要获取文档的绝对路径，
 * 如果直接使用本机绝对路径，更改了项目路径，就会导致出错，需要能够动态获取项目路径。
 * <p>
 * 源码结构符合 Maven 标准，例如：
 * <pre>
 *  peacetrue-test
 *     src
 *     ├── main
 *     │   ├── java
 *     │   └── resources
 *     └── test
 *         ├── java
 *         └── resources
 * </pre>
 *
 * @author peace
 */
public class SourcePathUtils {

    /** /src */
    public static final String PATH_SRC = File.separatorChar + "src";
    /** /main */
    public static final String PATH_MAIN = File.separatorChar + "main";
    /** /test */
    public static final String PATH_TEST = File.separatorChar + "test";
    /** /java */
    public static final String PATH_JAVA = File.separatorChar + "java";
    /** /resources */
    public static final String PATH_RESOURCES = File.separatorChar + "resources";
    /** /src/main */
    public static final String PATH_SRC_MAIN = PATH_SRC + PATH_MAIN;
    /** /src/main/java */
    public static final String PATH_SRC_MAIN_JAVA = PATH_SRC_MAIN + PATH_JAVA;
    /** /src/main/resources */
    public static final String PATH_SRC_MAIN_RESOURCES = PATH_SRC_MAIN + PATH_RESOURCES;
    /** /src/test */
    public static final String PATH_SRC_TEST = PATH_SRC + PATH_TEST;
    /** /src/test/java */
    public static final String PATH_SRC_TEST_JAVA = PATH_SRC_TEST + PATH_JAVA;
    /** /src/test/resources */
    public static final String PATH_SRC_TEST_RESOURCES = PATH_SRC_TEST + PATH_RESOURCES;

    private SourcePathUtils() {
    }

    /**
     * 转换类名（包名）为路径。
     * <p>
     * 例如：
     * <pre>
     * com.github.peacetrue.test.SourcePathUtils
     * -》
     * com/github/peacetrue/test/SourcePathUtils
     * </pre>
     *
     * @param clazz      类
     * @param isAbsolute 是否绝对路径，绝对路径添加前缀 {@link File#separatorChar}
     * @param isPackage  是否包名，包名不含 {@link Class#getSimpleName()}
     * @return 类名（包名）路径
     */
    public static String classToPath(Class<?> clazz, boolean isAbsolute, boolean isPackage) {
        String name = isPackage ? clazz.getPackage().getName() : clazz.getName();
        String path = name.replace('.', File.separatorChar);
        return isAbsolute ? File.separatorChar + path : path;
    }

    /**
     * 获取项目绝对路径。
     *
     * @return 项目绝对路径
     */
    public static String getProjectAbsolutePath() {
        return Objects.toString(System.getProperty("user.dir"), System.getenv("PWD"));
    }

    /**
     * 获取自定义绝对路径。
     *
     * @param isTest      是否测试目录
     * @param isResources 是否资源目录
     * @param paths       附加的路径
     * @return 自定义绝对路径
     */
    public static String getCustomAbsolutePath(boolean isTest, boolean isResources, String... paths) {
        StringBuilder builder = new StringBuilder(getProjectAbsolutePath())
                .append(PATH_SRC)
                .append(isTest ? PATH_TEST : PATH_MAIN)
                .append(isResources ? PATH_RESOURCES : PATH_JAVA);
        Arrays.asList(paths).forEach(builder::append);
        return builder.toString();
    }

    /**
     * 获取测试资源绝对路径。
     *
     * @param paths 附加的路径
     * @return 测试资源下绝对路径
     */
    public static String getTestResourceAbsolutePath(String... paths) {
        return getCustomAbsolutePath(true, true, paths);
    }

}
