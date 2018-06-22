package it.unimib.disco.aras.reportsservice.stream.consumer;

public interface Consumer<T> {
	void consume(T t);
}
