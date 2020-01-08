package com.github.peacetrue.test;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author xiayx
 */
public class TestUtilsTest {

    @Test
    public void getAbsoluteFolderFixed() {
        String expected = System.getenv("PWD") + "/src/test/java/com/github/peacetrue/test";
        Assert.assertEquals(expected, TestUtils.getAbsoluteFolder(getClass()));
    }

}