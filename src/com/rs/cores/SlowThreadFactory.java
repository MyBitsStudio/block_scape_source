package com.rs.cores;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A thread factory for spawning threads for the
 * slow executor service.
 * @ausky David O'Neill
 */
final class SlowThreadFactory implements ThreadFactory {

	private static final AtomicInteger poolNumber = new AtomicInteger(1);
	private final ThreadGroup group;
	private final AtomicInteger threadNumber = new AtomicInteger(1);
	private final String namePrefix;

	private final Thread.UncaughtExceptionHandler handler;

	SlowThreadFactory(Thread.UncaughtExceptionHandler handler) {
		this.handler = handler;

		SecurityHandler s = SecurityHandler.getHandler();
		group = (s != null) ? SecurityHandler.getThreadGroup() : Thread.currentThread().getThreadGroup();
		namePrefix = "Slow Pool-" + poolNumber.getAndIncrement() + "-thread-";
	}

	@Override
	public @NotNull Thread newThread(Runnable r) {
		Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
		if (t.isDaemon())
			t.setDaemon(false);
		if (t.getPriority() != Thread.MIN_PRIORITY)
			t.setPriority(Thread.MIN_PRIORITY);
		t.setUncaughtExceptionHandler(handler);
		return t;
	}
}