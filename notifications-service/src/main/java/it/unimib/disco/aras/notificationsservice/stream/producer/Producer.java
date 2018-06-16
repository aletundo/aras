package it.unimib.disco.aras.notificationsservice.stream.producer;

public interface Producer<T> {
    void dispatch(T t);
}
