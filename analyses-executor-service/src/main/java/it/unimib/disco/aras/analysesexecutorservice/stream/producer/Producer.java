package it.unimib.disco.aras.analysesexecutorservice.stream.producer;

public interface Producer<T> {
    void dispatch(T t);
}
