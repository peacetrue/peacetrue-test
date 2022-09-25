package com.github.peacetrue.test;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 线程断言测试工具类。
 *
 * @author peace
 **/
public class ThreadAssertionUtils {

    private ThreadAssertionUtils() {
    }

    /**
     * 捕获线程中抛出的断言错误。
     *
     * @return 断言错误。
     */
    public static Map<Thread, AssertionError> catchAssertionErrors() {
        Map<Thread, AssertionError> errors = new LinkedHashMap<>(1);
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            if (throwable instanceof AssertionError) errors.put(thread, (AssertionError) throwable);
        });
        return errors;
    }

    /**
     * 传播异常断言错误。
     *
     * @param errors 断言错误
     */
    public static void propagateAssertionError(Map<Thread, AssertionError> errors) {
        if (!errors.isEmpty()) throw errors.values().iterator().next();
    }

    /**
     * 传播异常，在父线程中抛出子线程中出现的断言错误。
     *
     * @param runnable 执行接口
     * @throws AssertionError 子线程中出现的断言错误
     */
    public static void propagateAssertionError(Runnable runnable) throws AssertionError {
        Map<Thread, AssertionError> errors = catchAssertionErrors();
        runnable.run();
        propagateAssertionError(errors);
    }

}
