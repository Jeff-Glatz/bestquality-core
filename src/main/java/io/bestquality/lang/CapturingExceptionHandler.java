package io.bestquality.lang;

import java.lang.Thread.UncaughtExceptionHandler;

public class CapturingExceptionHandler
        implements UncaughtExceptionHandler {
    private Throwable throwable;

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        this.throwable = throwable;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void assertSuccess()
            throws Throwable {
        if (throwable != null) {
            throw throwable;
        }
    }
}
