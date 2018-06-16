package it.unimib.disco.aras.analysesexecutorservice.stream.consumer;

public interface Consumer<T> {
	void consume(T t);
}
