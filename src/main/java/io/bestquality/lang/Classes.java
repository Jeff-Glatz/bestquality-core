package io.bestquality.lang;

import static java.lang.Thread.currentThread;

public class Classes {

    public static ClassLoader loaderOf(Class<?> caller) {
        ClassLoader loader = currentThread()
                .getContextClassLoader();
        if (loader == null) {
            loader = caller.getClassLoader();
        }
        return loader;
    }

    public static ClassLoader loaderOf(Object instance) {
        return loaderOf(instance.getClass());
    }
}
