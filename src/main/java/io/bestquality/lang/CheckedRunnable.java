package io.bestquality.lang;

@FunctionalInterface
public interface CheckedRunnable {
    void run() throws Exception;

    default Runnable asRunnable() {
        return () -> {
            try {
                run();
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
