package it.unimib.disco.aras.reportsservice.stream.producer;

public interface Producer<T> {
    void dispatch(T t);
}
