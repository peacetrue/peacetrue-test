package com.github.peacetrue.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.stream.Stream;

/**
 * @author peace
 **/
class JacocoUtilsTest {

    @Test
    void exclude() {
        Field[] fields = JacocoUtils.class.getDeclaredFields();
        Assertions.assertEquals(1, fields.length);
        Assertions.assertEquals(0, JacocoUtils.exclude(Stream.of(fields)).size());
    }
}
