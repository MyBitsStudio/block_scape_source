package com.rs.cores;


import org.jetbrains.annotations.NotNull;

/**
 * @ausky David O'Neill
 */
final class FastThreadHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread thread, @NotNull Throwable throwable) {
      //  Logger.threadFatal("(" + thread.getName() + ", fast pool) - Printing trace");
        throwable.printStackTrace();
    }
}
