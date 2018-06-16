package it.unimib.disco.aras.notificationsservice.stream.consumer;

public interface Consumer<T> {
	void consume(T t);
}
