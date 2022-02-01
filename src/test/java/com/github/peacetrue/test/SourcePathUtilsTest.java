package com.github.peacetrue.test;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.github.peacetrue.test.SourcePathUtils.*;

/**
 * @author peace
 */
class SourcePathUtilsTest {

    @Test
    void paths() {
        List<String> paths = Arrays.asList(
                PATH_SRC,
                PATH_SRC_MAIN,
                PATH_SRC_MAIN_JAVA,
                PATH_SRC_MAIN_RESOURCES,
                PATH_SRC_TEST,
                PATH_SRC_TEST_JAVA,
                PATH_SRC_TEST_RESOURCES
        );
        Assertions.assertEquals(
                "/src\n" +
                        "/src/main\n" +
                        "/src/main/java\n" +
                        "/src/main/resources\n" +
                        "/src/test\n" +
                        "/src/test/java\n" +
                        "/src/test/resources",
                String.join("\n", paths));
    }

    @Test
    void getProjectAbsolutePath() {
        Assertions.assertTrue(SourcePathUtils.getProjectAbsolutePath().endsWith("peacetrue-test"));
    }

    @Test
    void getCustomAbsolutePath() {
        String absolutePath = SourcePathUtils.getCustomAbsolutePath(true, true, "/test");
        Assertions.assertTrue(absolutePath.endsWith("/src/test/resources/test"));
    }

    @Test
    void getTestResourceAbsolutePath() {
        String absolutePath = SourcePathUtils.getTestResourceAbsolutePath("/test");
        Assertions.assertTrue(absolutePath.endsWith("/src/test/resources/test"));
    }
}
