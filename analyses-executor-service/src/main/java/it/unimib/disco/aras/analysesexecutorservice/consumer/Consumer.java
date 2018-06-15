package it.unimib.disco.aras.analysesexecutorservice.consumer;

public interface Consumer<T> {
	void consume(T t);
}
