package io.bestquality.lang;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CapturingExceptionHandlerTest {
    private CapturingExceptionHandler handler;

    @Before
    public void setUp() {
        handler = new CapturingExceptionHandler();
    }

    @Test
    public void shouldCaptureUncaughtException()
            throws Exception {
        Error error = new Error("boom");

        Thread thread = new Thread(() -> {
            throw error;
        });
        thread.setUncaughtExceptionHandler(handler);
        thread.start();
        thread.join();

        assertThat(handler.getThrowable())
                .isSameAs(error);
    }

    @Test(expected = Error.class)
    public void shouldFailAssertionWhenUncaughtExceptionIsPresent()
            throws Throwable {
        Error error = new Error("boom");

        Thread thread = new Thread(() -> {
            throw error;
        });
        thread.setUncaughtExceptionHandler(handler);
        thread.start();
        thread.join();

        handler.assertSuccess();
    }

    @Test
    public void shouldPassAssertionWhenPropagateUncaughtExceptionIsNotPresent()
            throws Throwable {
        Thread thread = new Thread(() -> {
        });
        thread.setUncaughtExceptionHandler(handler);
        thread.start();
        thread.join();

        handler.assertSuccess();
    }
}
