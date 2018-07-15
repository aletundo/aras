package it.unimib.disco.aras.reportsservice.stream.consumer;

/**
 * The Interface Consumer.
 *
 * @param <T>
 *            the generic type
 */
public interface Consumer<T> {
	
	/**
	 * Consume.
	 *
	 * @param t
	 *            the t
	 */
	void consume(T t);
}
