package io.bestquality.lang;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

public class CheckedConsumerTest {
    private AtomicReference<String> reference;

    @Before
    public void setUp() {
        reference = new AtomicReference<>();
    }

    @Test
    public void andThenShouldChain()
            throws Exception {
        CheckedConsumer<String> composed = (value) -> {
        };
        composed = composed.andThen(reference::set);

        composed.accept("Hello World");

        assertThat(reference.get())
                .isEqualTo("Hello World");
    }

    @Test
    public void asConsumerShouldDelegate() {
        CheckedConsumer<String> consumer = reference::set;

        consumer.asConsumer()
                .accept("Hello World");

        assertThat(reference.get())
                .isEqualTo("Hello World");
    }

    @Test(expected = IllegalArgumentException.class)
    public void asConsumerShouldForwardRuntimeException()
            throws Exception {
        CheckedConsumer<String> consumer = (message) -> {
            throw new IllegalArgumentException(message);
        };

        consumer.asConsumer()
                .accept("Hello World");
    }

    @Test(expected = RuntimeException.class)
    public void asConsumerShouldWrapWithRuntimeException()
            throws Exception {
        CheckedConsumer<String> consumer = (message) -> {
            throw new Exception(message);
        };

        consumer.asConsumer()
                .accept("Hello World");
    }
}
