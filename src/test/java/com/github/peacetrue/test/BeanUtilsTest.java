package com.github.peacetrue.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.peacetrue.beanmap.BeanMap;
import com.github.peacetrue.beanmap.BeanMapUtils;
import com.github.peacetrue.lang.CommandUtils;
import com.github.peacetrue.util.MapUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.util.Map;

/**
 * 如何查看 {@link PosixFileAttributes} 的属性。
 *
 * @author peace
 **/
@Slf4j
class BeanUtilsTest {

    private static final Path ANTORA_DIR = Paths.get(SourcePathUtils.getProjectAbsolutePath()).resolve("docs/antora");
    private static final Path EXAMPLES_DIR = ANTORA_DIR.resolve("modules/ROOT/examples");
    private static final Path BEAN_UTILS_DIR = EXAMPLES_DIR.resolve("BeanUtils");

    @BeforeAll
    @SneakyThrows
    static void beforeAll() {
        Files.createDirectories(BEAN_UTILS_DIR);
    }

    @AfterAll
    static void afterAll() {
//        runAntora();
    }

    @SneakyThrows
    private static void runAntora() {
        new ProcessBuilder()
                .directory(ANTORA_DIR.toFile())
                .command(CommandUtils.sh("antora generate playbook.yml && open -a 'Google Chrome' build/site/peacetrue-test/reference.html"))
                .start()
                .waitFor();
    }

    @SneakyThrows
    private static PosixFileAttributes getPosixFileAttributes() {
        String gradlewPath = SourcePathUtils.getProjectAbsolutePath() + "/gradlew";
        return Files.readAttributes(Paths.get(gradlewPath), PosixFileAttributes.class);
    }

    /** 通过 debug 查看 */
    @Test
    @SneakyThrows
    void viewByDebug() {
        PosixFileAttributes attributes = getPosixFileAttributes();
        Assertions.assertNotNull(attributes);
    }

    //tag::toString[]

    /** 通过 {@link Object#toString()} 查看 */
    @Test
    @SneakyThrows
    void viewByToString() {
        PosixFileAttributes attributes = getPosixFileAttributes();
        String string = attributes.toString();
        Files.write(BEAN_UTILS_DIR.resolve("toString.txt"), string.getBytes(StandardCharsets.UTF_8));
    }
    //end::toString[]

    //tag::jackson[]

    /** 通过 jackson 查看 */
    @Test
    @SneakyThrows
    void viewByJackson() {
        PosixFileAttributes attributes = getPosixFileAttributes();
        String jackson = viewByJackson(attributes);
        byte[] bytes = jackson.getBytes(StandardCharsets.UTF_8);
        Files.write(BEAN_UTILS_DIR.resolve("jackson.txt"), bytes);
    }
    //end::jackson[]

    //tag::custom[]

    /** 测试自定义 view 方法 */
    @Test
    @SneakyThrows
    void viewByCustom() {
        PosixFileAttributes attributes = getPosixFileAttributes();
        Map<String, Object> map = EvolutionBeanUtils.view(attributes);
        String prettify = MapUtils.prettify(map);
        byte[] bytes = prettify.getBytes(StandardCharsets.UTF_8);
        Files.write(BEAN_UTILS_DIR.resolve("custom.txt"), bytes);
    }
    //end::custom[]


    private static PosixFileAttributeView getPosixFileAttributeView() {
        String gradlewPath = SourcePathUtils.getProjectAbsolutePath() + "/gradlew";
        return Files.getFileAttributeView(Paths.get(gradlewPath), PosixFileAttributeView.class);
    }

    //tag::customNestChallenge[]

    /** 自定义 view 方法不能处理嵌套对象 */
    @Test
    @SneakyThrows
    void viewByCustomNotSupportNest() {
        PosixFileAttributeView view = getPosixFileAttributeView();
        Map<String, Object> map = EvolutionBeanUtils.view(view);
        String prettify = MapUtils.prettify(map);
        byte[] bytes = prettify.getBytes(StandardCharsets.UTF_8);
        Files.write(BEAN_UTILS_DIR.resolve("customNestChallenge.txt"), bytes);
    }
    //end::customNestChallenge[]


    //tag::customNest[]

    /** 查看嵌套对象 */
    @Test
    @SneakyThrows
    void viewNestObject() {
        PosixFileAttributeView view = getPosixFileAttributeView();
        Map<String, Object> map = (Map<String, Object>) EvolutionBeanUtils.viewNest(view);
        String prettify = MapUtils.prettify(BeanMapUtils.flatten(map));
        byte[] bytes = prettify.getBytes(StandardCharsets.UTF_8);
        Files.write(BEAN_UTILS_DIR.resolve("customNest.txt"), bytes);
    }
    //end::customNest[]

    @SneakyThrows
    private static String viewByJackson(Object value) {
        return new ObjectMapper()
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .registerModule(new JavaTimeModule())
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(value);
    }

}
