package com.github.peacetrue.test;

/**
 * 测试工具类
 *
 * @author xiayx
 */
public abstract class TestUtils {

    /**
     * 获取类对应源码所在的绝对目录
     * <p>
     * 限标准maven结构下的单元测试使用
     *
     * @param clazz 类
     * @return 绝对目录
     */
    public static String getAbsoluteFolder(Class<?> clazz) {
        String path = clazz.getResource(clazz.getSimpleName() + ".class").getPath();
        String[] parts = path.split("/build/classes/java/", 2);
        if (parts[1].startsWith("main")) {
            parts[1] = "/src/main/java" + parts[1].substring("main".length());
        } else if (parts[1].startsWith("test")) {
            parts[1] = "/src/test/java" + parts[1].substring("test".length());
        }
        parts[1] = parts[1].substring(0, parts[1].length() - 1 - clazz.getSimpleName().length() - ".class".length());
        return String.join("", parts);
    }


}
