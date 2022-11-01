package com.github.peacetrue.test;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFileAttributes;
import java.util.Map;

/**
 * @author peace
 **/
@Slf4j
class BeanUtilsTest {

    @Test
    @SneakyThrows
    void view() {
        String gradlewPath = SourcePathUtils.getProjectAbsolutePath() + "/gradlew";
        PosixFileAttributes attributes = Files.readAttributes(Paths.get(gradlewPath), PosixFileAttributes.class);
        log.info("attributes.toString(): {}", attributes.toString());
        log.info("attributes.permissions: {}", attributes.permissions());
        Map<String, Object> info = BeanUtils.view(attributes);
        log.info("attributes(view): {}", info);

        Object object = new Object();
        Assertions.assertThrows(IllegalStateException.class, () -> BeanUtils.view(object));
    }

}
