package com.github.peacetrue.test;


import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static com.github.peacetrue.test.SourcePathUtils.*;

/**
 * @author peace
 */
class SourcePathUtilsTest {

    @Test
    void classToPath() {
        Assertions.assertEquals("/com/github/peacetrue/test",
                SourcePathUtils.classToPath(SourcePathUtilsTest.class, true, true));
        Assertions.assertEquals("/com/github/peacetrue/test/SourcePathUtilsTest",
                SourcePathUtils.classToPath(SourcePathUtilsTest.class, true, false));
        Assertions.assertEquals("com/github/peacetrue/test",
                SourcePathUtils.classToPath(SourcePathUtilsTest.class, false, true));
        Assertions.assertEquals("com/github/peacetrue/test/SourcePathUtilsTest",
                SourcePathUtils.classToPath(SourcePathUtilsTest.class, false, false));
    }

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

    //tag::getProjectAbsolutePath[]

    /** 获取当前项目绝对路径 */
    @Test
    void getProjectAbsolutePath() {
        String projectAbsolutePath = SourcePathUtils.getProjectAbsolutePath();
        Assertions.assertTrue(Files.exists(Paths.get(projectAbsolutePath)), "存在当前项目目录");
        Assertions.assertTrue(projectAbsolutePath.endsWith("peacetrue-test"), "当前项目目录为 peacetrue-test");
    }
    //end::getProjectAbsolutePath[]

    @Test
    void getCustomAbsolutePath() {
        String absolutePath = SourcePathUtils.getCustomAbsolutePath(false, false, "/test");
        Assertions.assertTrue(absolutePath.endsWith("/src/main/java/test"));
        absolutePath = SourcePathUtils.getCustomAbsolutePath(false, true, "/test");
        Assertions.assertTrue(absolutePath.endsWith("/src/main/resources/test"));
        absolutePath = SourcePathUtils.getCustomAbsolutePath(true, false, "/test");
        Assertions.assertTrue(absolutePath.endsWith("/src/test/java/test"));
        absolutePath = SourcePathUtils.getCustomAbsolutePath(true, true, "/test");
        Assertions.assertTrue(absolutePath.endsWith("/src/test/resources/test"));
    }

    //tag::getTestResourceAbsolutePath[]

    /** 获取测试资源的绝对路径 */
    @Test
    void getTestResourceAbsolutePath() {
        String absolutePath = SourcePathUtils.getTestResourceAbsolutePath("/test");
        Assertions.assertTrue(
                absolutePath.endsWith("/peacetrue-test/src/test/resources/test"),
                "Maven 标准源码结构的测试资源目录"
        );
    }
    //end::getTestResourceAbsolutePath[]


    /** 帮助 Antora 文档 */
//    @Test
    @SneakyThrows
    void helpAntora() {
        // 输出 Antora 文档所需的项目文件结构
        String projectAbsolutePath = SourcePathUtils.getProjectAbsolutePath();
        new ProcessBuilder()
                .directory(new File(projectAbsolutePath))
                .command("tree", "-L", "2", "src")
                .redirectOutput(new File(projectAbsolutePath + "/docs/antora/modules/ROOT/examples/structure.txt"))
                .start()
                .waitFor();
    }
}
