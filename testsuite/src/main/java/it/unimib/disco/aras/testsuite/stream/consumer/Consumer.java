package it.unimib.disco.aras.testsuite.stream.consumer;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public interface Consumer<T> {
	void consume(T t);
	CountDownLatch getLatch();
	void setLatch(CountDownLatch latch);
	List<T> getMessages();
}
