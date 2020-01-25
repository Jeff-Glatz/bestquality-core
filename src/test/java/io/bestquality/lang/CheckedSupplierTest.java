package io.bestquality.lang;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CheckedSupplierTest {

    @Test(expected = CheckedException.class)
    public void shouldWorkForCheckedMethodReferences()
            throws Exception {
        CheckedSupplier<CheckedConstructor> supplier = CheckedConstructor::new;

        supplier.get();
    }

    @Test
    public void shouldWorkForUncheckedMethodReferences()
            throws Exception {
        CheckedSupplier<UncheckedConstructor> supplier = UncheckedConstructor::new;

        assertThat(supplier.get())
                .isNotNull();
    }

    public static class CheckedException
            extends Exception {
    }

    public static class CheckedConstructor {
        public CheckedConstructor() throws Exception {
            throw new CheckedException();
        }
    }

    public static class UncheckedConstructor {
        public UncheckedConstructor() {
        }
    }
}
