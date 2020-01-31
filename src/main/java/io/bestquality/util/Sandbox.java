package io.bestquality.util;

import io.bestquality.lang.CapturingExceptionHandler;
import io.bestquality.lang.CheckedRunnable;

import java.net.URL;
import java.net.URLClassLoader;

import static io.bestquality.lang.Classes.loaderOf;
import static java.lang.Thread.currentThread;
import static java.util.Arrays.stream;

public class Sandbox {
    private ClassLoader loader;

    public Sandbox withClassLoader(ClassLoader loader) {
        this.loader = loader;
        return this;
    }

    public Sandbox withClassesIn(URL... urls) {
        return withClassLoader(new URLClassLoader(urls, loaderOf(this)));
    }

    public Sandbox withClassesIn(String... resources) {
        ClassLoader loader = loaderOf(this);
        return withClassesIn(stream(resources)
                .map(loader::getResource)
                .toArray(URL[]::new));
    }

    public void execute(CheckedRunnable runnable)
            throws Throwable {
        CapturingExceptionHandler handler = new CapturingExceptionHandler();
        Thread executor = new Thread(() -> {
            try {
                runnable.run();
            } catch (Exception e) {
                handler.uncaughtException(currentThread(), e);
            }
        });
        executor.setContextClassLoader(loader);
        executor.setUncaughtExceptionHandler(handler);
        executor.start();
        executor.join();
        handler.assertSuccess();
    }
}
