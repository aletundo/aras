/*
 * 
 */
package it.unimib.disco.aras.analysesconfiguratorservice.stream.producer;

/**
 * The Interface Producer.
 *
 * @param <T>
 *            the generic type
 */
public interface Producer<T> {
    
    /**
	 * Dispatch.
	 *
	 * @param t
	 *            the t
	 */
    void dispatch(T t);
}
