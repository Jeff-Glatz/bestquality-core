package io.bestquality.lang;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.net.URLClassLoader;

import static io.bestquality.lang.Classes.loaderOf;
import static java.lang.Thread.currentThread;
import static org.assertj.core.api.Assertions.assertThat;

public class ClassesTest {
    private CapturingExceptionHandler handler;

    @Before
    public void setUp() {
        handler = new CapturingExceptionHandler();
    }

    @After
    public void tearDown()
            throws Throwable {
        handler.assertSuccess();
    }

    @Test
    public void shouldReturnThreadContextClassLoader()
            throws Exception {
        ClassLoader loader = new URLClassLoader(new URL[0]);
        Thread runner = new Thread(() -> {
            assertThat(loaderOf(this))
                    .isSameAs(loader);
        });
        runner.setContextClassLoader(loader);
        runner.setUncaughtExceptionHandler(handler);
        runner.start();
        runner.join();
    }

    @Test
    public void shouldReturnClassLoaderThatLoadedClass()
            throws Exception {
        Thread runner = new Thread(() -> {
            Thread thread = currentThread();
            ClassLoader original = thread.getContextClassLoader();
            thread.setContextClassLoader(null);
            try {
                assertThat(thread.getContextClassLoader())
                        .isNull();

                ClassLoader actual = loaderOf(this);
                assertThat(actual)
                        .isSameAs(getClass().getClassLoader());
            } finally {
                thread.setContextClassLoader(original);
            }
        });
        runner.setUncaughtExceptionHandler(handler);
        runner.start();
        runner.join();
    }
}
