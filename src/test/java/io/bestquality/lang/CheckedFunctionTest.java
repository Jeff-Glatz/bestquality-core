package io.bestquality.lang;

import org.junit.Test;

import static java.lang.String.valueOf;
import static org.assertj.core.api.Assertions.assertThat;

public class CheckedFunctionTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldCompose()
            throws Exception {
        CheckedFunction<String, Boolean> function = Boolean::parseBoolean;
        CheckedFunction<Integer, String> converter = (value) -> {
            switch (value) {
                case 0:
                    return "False";
                case 1:
                    return "True";
                default:
                    throw new IllegalArgumentException(valueOf(value));
            }
        };

        CheckedFunction<Integer, Boolean> composed = function.compose(converter);

        assertThat(composed.apply(0))
                .isFalse();
        assertThat(composed.apply(1))
                .isTrue();
        composed.apply(2);
    }

    @Test
    public void andThenShouldChain()
            throws Exception {
        CheckedFunction<String, Boolean> function = Boolean::parseBoolean;
        CheckedFunction<String, String> mapper = function.andThen(String::valueOf);

        assertThat(mapper.apply("True"))
                .isEqualTo("true");
    }

    @Test
    public void asFunctionShouldDelegate() {
        CheckedFunction<String, Boolean> function = Boolean::parseBoolean;

        assertThat(function.asFunction()
                .apply("True"))
                .isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void asFunctionShouldForwardRuntimeException() {
        CheckedFunction<String, Boolean> function = (message) -> {
            throw new IllegalArgumentException(message);
        };

        function.asFunction()
                .apply("Hello World");
    }

    @Test(expected = RuntimeException.class)
    public void asFunctionShouldWrapWithRuntimeException() {
        CheckedFunction<String, Boolean> function = (message) -> {
            throw new Exception(message);
        };

        function.asFunction()
                .apply("Hello World");
    }
}
