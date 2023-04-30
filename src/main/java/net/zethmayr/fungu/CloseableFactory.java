package net.zethmayr.fungu;

import net.zethmayr.fungu.throwing.ThrowingConsumer;

import java.io.Closeable;
import java.lang.reflect.Proxy;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static net.zethmayr.fungu.core.ExceptionFactory.becauseAdaptersOnly;
import static net.zethmayr.fungu.core.ExceptionFactory.becauseIllegal;
import static net.zethmayr.fungu.UponHelper.throwingUpon;

/**
 * Adapts closeable resources into more convenient forms.
 */
public final class CloseableFactory {

    private CloseableFactory() {
        throw becauseAdaptersOnly();
    }

    /**
     * Adapts a resource that
     * needs to be closed
     * but does not already conform to the {@link Closeable} interface
     * to an instance of {@link Closeable}.
     *
     * @param oddlyClosed a non-conforming resource
     * @param doesClose   the method for disposing of the resource
     * @param <T>         the resource type
     * @return a closeable
     */
    public static <T> Closeable closeable(final T oddlyClosed, final Consumer<T> doesClose) {
        return () -> doesClose.accept(oddlyClosed);
    }

    /**
     * Adapts a closeable resource to an instance with the same interfaces,
     * but which redirects the {@link Closeable#close()} method
     * to a consumer.
     *
     * @param resourceInterface    the primary interface for the resource
     * @param closeable            the resource instance
     * @param interceptor          the intercepting close method
     * @param additionalInterfaces any additional interfaces that need to be supported
     * @param <T>                  the closeable resource interface type
     * @param <R>                  the concrete resource type
     * @return a similar instance with close redirected.
     */
    public static <T extends Closeable, R extends T> T closeIntercepted(
            Class<T> resourceInterface, final R closeable, final Consumer<R> interceptor, final Class<?>... additionalInterfaces
    ) {
        return closeIntercepted(resourceInterface, closeable, (ThrowingConsumer<R>) interceptor::accept, additionalInterfaces);
    }

    /**
     * Adapts a closeable resource to an instance with the same interfaces,
     * but which redirects the {@link Closeable#close()} method
     * to an implementation which may throw exceptions.
     *
     * @param resourceInterface    the primary interface for the resource
     * @param closeable            the resource instance
     * @param interceptsClose      the intercepting close method
     * @param additionalInterfaces any additional interfaces that need to be supported
     * @param <T>                  the closeable resource interface type
     * @param <R>                  the concrete resource type
     * @return a similar instance with close redirected.
     */
    public static <T extends Closeable, R extends T> T closeIntercepted(
            Class<T> resourceInterface, final R closeable, final ThrowingConsumer<R> interceptsClose, final Class<?>... additionalInterfaces
    ) {
        if (!resourceInterface.isInterface()) {
            throw becauseIllegal("%s is not an interface", resourceInterface);
        }
        return resourceInterface.cast(Proxy.newProxyInstance(
                closeable.getClass().getClassLoader(),
                Stream.of(
                                Stream.of(closeable.getClass().getInterfaces()),
                                Stream.of(Closeable.class),
                                Stream.of(additionalInterfaces)
                        )
                        .flatMap(s -> (Stream<Class<?>>) s)
                        .filter(Class::isInterface)
                        .distinct()
                        .toArray(Class[]::new),
                (p, m, a) ->
                        m.getName().equals("close")
                                ? throwingUpon(null, (ThrowingConsumer<T>) x -> interceptsClose.accept(closeable))
                                : m.invoke(closeable, a)
        ));
    }
}
