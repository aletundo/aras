package it.unimib.disco.aras.analysesexecutorservice.producer;

public interface Producer<T> {
    void dispatch(T t);
}
