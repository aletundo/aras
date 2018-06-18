package it.unimib.disco.aras.analysesconfiguratorservice.stream.producer;

public interface Producer<T> {
    
    void dispatch(T t);
}
