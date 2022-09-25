package com.github.peacetrue.test;

import org.jooq.lambda.Unchecked;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;

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
        Thread thread2 = new Thread(() -> {
            throw new NullPointerException();
        });
        thread2.start();
        Stream.of(thread, thread2).forEach(Unchecked.consumer(Thread::join));
    }

    @Test
    void catchAssertionErrors() {
        Map<Thread, AssertionError> errors = ThreadAssertionUtils.catchAssertionErrors();
        run();
        Assertions.assertThrows(AssertionError.class, () ->
                ThreadAssertionUtils.propagateAssertionError(errors)
        );
    }

    @Test
    void propagateAssertionError() {
        Assertions.assertThrows(Throwable.class, () ->
                ThreadAssertionUtils.propagateAssertionError(this::run)
        );
        Assertions.assertDoesNotThrow(() ->
                ThreadAssertionUtils.propagateAssertionError(() -> {
                })
        );
    }

}
