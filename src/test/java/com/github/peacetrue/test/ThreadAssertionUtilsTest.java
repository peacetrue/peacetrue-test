package com.github.peacetrue.test;

import org.jooq.lambda.Unchecked;
import org.jooq.lambda.fi.util.function.CheckedConsumer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author peace
 **/
class ThreadAssertionUtilsTest {

    /**
     * 线程中断言失败，不影响主线程。
     * 这样不合适，需要传播到主线程。
     */
    @Test
    void basic() {
        run();
    }

    private void run() {
        Thread thread = new Thread(() -> fail("just fail!"));
        thread.start();
        Unchecked.consumer((CheckedConsumer<Thread>) Thread::join).accept(thread);
    }

    @Test
    void catchAssertionErrors() {
        Map<Thread, AssertionError> errors = ThreadAssertionUtils.catchAssertionErrors();
        run();
        Assertions.assertThrows(Throwable.class, () ->
                ThreadAssertionUtils.propagateAssertionError(errors)
        );
    }

    @Test
    void propagateAssertionError() {
        Assertions.assertThrows(Throwable.class, () ->
                ThreadAssertionUtils.propagateAssertionError(this::run)
        );
    }

}
