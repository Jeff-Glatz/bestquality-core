package io.bestquality.util;

import org.junit.Test;

import static io.bestquality.lang.Classes.loaderOf;
import static io.bestquality.net.MaskingClassLoader.maskingClasses;
import static java.lang.Class.forName;
import static org.assertj.core.api.Assertions.assertThat;

public class SandboxTest {

    @Test(expected = Error.class)
    public void shouldPropagateUncaughtExceptionAfterExecution()
            throws Throwable {
        new Sandbox().execute(() -> {
            throw new Error();
        });
    }

    @Test(expected = ClassNotFoundException.class)
    public void shouldUseClassLoaderToExecuteCheckedRunnable()
            throws Throwable {
        new Sandbox().withClassLoader(maskingClasses(Custom.class))
                .execute(() -> {
                    forName("io.bestquality.util.SandboxTest.Custom", false, null);
                });
    }

    @Test
    public void shouldAddResourceToClasspath()
            throws Throwable {
        new Sandbox().withClassesIn("stubs/cucumber-stub.jar")
                .execute(() -> {
                    ClassLoader loader = loaderOf(this);
                    Class<?> type = loader.loadClass("io.cucumber.spring.CucumberTestContext");
                    assertThat(type.getName())
                            .isEqualTo("io.cucumber.spring.CucumberTestContext");
                });
    }

    private static class Custom {
    }
}
