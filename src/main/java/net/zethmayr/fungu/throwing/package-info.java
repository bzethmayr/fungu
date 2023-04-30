/**
 * Adapts methods throwing checked exceptions
 * to analogues deferring throw.
 * Example nonsense usage:
 * <pre>
 * final Closeable resource = getResource();
 * final Sink thrown = sink();
 * Optional.of(resource)
 *    .ifPresent(sinking((ThrowingConsumer&lt;Closeable&gt;)Closeable::close, thrown));
 * thrown.raise();
 * </pre>
 */
package net.zethmayr.fungu.throwing;