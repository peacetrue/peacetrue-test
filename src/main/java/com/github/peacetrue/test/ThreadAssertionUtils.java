package com.github.peacetrue.test;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 线程内断言测试工具类。
 * <p>
 * 在子线程内执行测试时，断言失败后，断言错误仅在子线程内部被提示。
 * 测试框架无法处理子线程内的断言错误，
 * 需要捕获子线程内的断言错误，抛出到主线程中，这样测试框架才能正常处理。
 *
 * @author peace
 **/
public class ThreadAssertionUtils {

    private ThreadAssertionUtils() {
    }

    /**
     * 捕获子线程中抛出的断言错误。
     *
     * @return 断言错误集合
     */
    public static Map<Thread, AssertionError> catchAssertionErrors() {
        Map<Thread, AssertionError> errors = new LinkedHashMap<>(1);
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            if (throwable instanceof AssertionError) errors.put(thread, (AssertionError) throwable);
        });
        return errors;
    }

    /**
     * 抛出断言错误。如果存在多个，仅抛出第一个。
     *
     * @param errors 断言错误集合
     */
    public static void propagateAssertionError(Map<Thread, AssertionError> errors) {
        if (!errors.isEmpty()) throw errors.values().iterator().next();
    }

    /**
     * 抛出断言错误，在主线程中抛出子线程中出现的断言错误。
     *
     * @param runnable 执行接口，内部开启子线程，并在子线程中执行测试断言
     */
    public static void propagateAssertionError(Runnable runnable) {
        Map<Thread, AssertionError> errors = catchAssertionErrors();
        runnable.run();
        propagateAssertionError(errors);
    }

}
