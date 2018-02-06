package it.unimib.disco.aras.analysesconfiguratorservice.producer;

public interface Producer<T> {
    
    void dispatch(T t);
}
