package io.bestquality.lang;

import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

@FunctionalInterface
public interface CheckedConsumer<T> {
    void accept(T value) throws Exception;

    default Consumer<T> asConsumer() {
        return (T t) -> {
            try {
                accept(t);
            } catch (Error | RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    default CheckedConsumer<T> andThen(CheckedConsumer<? super T> after) {
        requireNonNull(after);
        return (T t) -> {
            accept(t);
            after.accept(t);
        };
    }
}
