package it.unimib.disco.aras.testsuite.stream.consumer;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;

public interface Consumer<T> {
	void consume(T t);
	CountDownLatch getLatch();
	ConcurrentMap<Long, T> getMessages();
}
