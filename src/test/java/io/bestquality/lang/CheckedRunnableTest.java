package io.bestquality.lang;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;

public class CheckedRunnableTest {

    @Test
    public void asFunctionShouldDelegate()
            throws Exception {
        AtomicBoolean ran = new AtomicBoolean(false);
        CheckedRunnable runnable = () -> {
            ran.set(true);
        };

        runnable.asRunnable()
                .run();

        assertThat(ran.get())
                .isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void asFunctionShouldForwardRuntimeException() {
        CheckedRunnable runnable = () -> {
            throw new IllegalArgumentException("boom");
        };

        runnable.asRunnable()
                .run();
    }

    @Test(expected = RuntimeException.class)
    public void asFunctionShouldWrapWithRuntimeException() {
        CheckedRunnable runnable = () -> {
            throw new Exception("boom");
        };

        runnable.asRunnable()
                .run();
    }
}
