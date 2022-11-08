package com.github.peacetrue.test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Jacoco 工具类。
 *
 * @author peace
 **/
public class JacocoUtils {

    private JacocoUtils() {
    }

    /**
     * 排除字段集合 fields 中，由 Jacoco 注入的字段。
     *
     * @param fields 字段集合
     * @return 字段列表
     */
    public static List<Field> exclude(Stream<Field> fields) {
        return fields
                .filter(field -> !field.getName().startsWith("$jacoco"))
                .collect(Collectors.toList());
    }

}
