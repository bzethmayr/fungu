package net.zethmayr.fungu.arc;

import net.zethmayr.fungu.throwing.Sink;
import net.zethmayr.fungu.throwing.ThrowingConsumer;

import java.io.Closeable;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static net.zethmayr.fungu.core.ExceptionFactory.nonexistentBecause;
import static net.zethmayr.fungu.core.SupplierFactory.from;
import static net.zethmayr.fungu.throwing.SinkFactory.sink;
import static net.zethmayr.fungu.throwing.Sinkable.sinking;

/**
 * A counted reference controls access to a resource
 * of which only one is needed per thread.
 * The resource itself is closed when the last reference is closed.
 *
 * @param <T> the resource type.
 */
public abstract class CountedReference<T extends Closeable> implements AutoCloseable {

    /**
     * Creates a new thread-local for static use by implementations.
     * @return a new, empty thread-local
     * @param <T> the resource type.
     */
    protected static <T extends Closeable> ThreadLocal<CountAndRef<T>> newLocalCount() {
        return new ThreadLocal<>();
    }

    /**
     * Each new instance of a given counted ref increases the reference count.
     */
    protected CountedReference() {
        maybeRef()
                .map(CountAndRef::incremented)
                .map(CountAndRef::getResource)
                .orElseGet(() -> {
                    final T resource = createResource();
                    countAndRef().set(new CountAndRef<>(resource));
                    return resource;
                });
    }

    /**
     * Returns a consistent reference to a compatible thread-local,
     * which should be statically defined.
     * @return the thread-local
     */
    protected abstract ThreadLocal<CountAndRef<T>> countAndRef();

    /**
     * Creates the actual, real resource and returns the reference.
     *
     * @return the real resource
     */
    protected abstract T createResource();

    /**
     * Called when the last reference is closed.
     * By default, just calls {@link Closeable#close()} on the resource,
     * per the convention that the resource will account for all of its own disposal.
     * If this is not entirely the case, this method can be overridden.
     * @param resource a resource to dispose of
     * @throws IOException if resource disposal fails.
     */
    protected void disposeResource(final T resource) throws IOException {
        resource.close();
    }

    /**
     * Returns a reference to the resource for use,
     * without incrementing the count.
     * @return the resource.
     */
    public T getResource() {
        return maybeRef()
                .map(CountAndRef::getResource)
                .orElseThrow(nonexistentBecause("Resource not open in %s", from(this)));
    }

    /**
     * Closes this reference and decrements the count,
     * closing the real resource when the count reaches zero.
     * It is often useful to proxy the real resource such that
     * the exposed close method calls this method.
     * @throws Exception as thrown by the resource {@link Closeable#close()} method.
     */
    @Override
    public final void close() throws Exception {
        final Sink closeThrew = sink();
        maybeRef()
                .map(CountAndRef::decrementedZero)
                .map(CountAndRef::getResource)
                .ifPresent(sinking((ThrowingConsumer<T, IOException>)this::internalDispose, closeThrew));
        closeThrew.raise();
    }

    private void internalDispose(final T resource) throws IOException {
        countAndRef().remove();
        disposeResource(resource);
    }

    private Optional<CountAndRef<T>> maybeRef() {
        return Optional.of(countAndRef())
                .map(ThreadLocal::get);
    }

    /**
     * Holds the resource and count while a resource is in use.
     *
     * @param <T> the resource type
     */
    protected static final class CountAndRef<T extends Closeable> {
        private final AtomicLong count = new AtomicLong(1L);

        private final T resource;

        CountAndRef(final T resource) {
            this.resource = resource;
        }

        CountAndRef<T> incremented() {
            count.incrementAndGet();
            return this;
        }

        CountAndRef<T> decrementedZero() {
            return count.decrementAndGet() < 1L
                    ? this
                    : null;
        }

        T getResource() {
            return resource;
        }
    }
}
