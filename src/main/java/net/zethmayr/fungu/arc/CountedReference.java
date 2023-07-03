package net.zethmayr.fungu.arc;

import net.zethmayr.fungu.core.declarations.ReuseResults;
import net.zethmayr.fungu.throwing.Sink;
import net.zethmayr.fungu.throwing.ThrowingConsumer;

import java.io.Closeable;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static net.zethmayr.fungu.throwing.SinkFactory.sink;
import static net.zethmayr.fungu.throwing.Sinkable.sinking;

/**
 * A counted reference controls access to a resource
 * of which only one is needed per thread.
 * Each new instance of a counted reference increments the reference count.
 * The resource itself is opened with the first reference,
 * and closed when the last reference is closed.
 *
 * @param <T> the resource type.
 */

public abstract class CountedReference<T> implements AutoCloseable {

    /**
     * Creates a new open counted reference to the open resource
     * and increments the reference count.
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
     * Returns the local count and internal reference.
     *
     * @return the local count and reference, if present.
     */
    protected Optional<CountAndRef<T>> maybeRef() {
        return Optional.of(countAndRef())
                .map(ThreadLocal::get);
    }

    /**
     * After the last usage,
     * removes the local counter and disposes of the resource.
     *
     * @param resource the real resource
     * @throws IOException as thrown by {@literal e.g.} {@link Closeable#close()}.
     */
    protected void internalDispose(final T resource) throws IOException {
        countAndRef().remove();
        disposeResource(resource);
    }

    /**
     * By default,
     * calls {@link #decrementUsage(T)}.
     *
     * @throws IOException as thrown by {@literal e.g.} the resource {@link Closeable#close()} method.
     */
    @Override
    public void close() throws IOException {
        decrementUsage(null);
    }

    /**
     * Closes this reference and decrements the count,
     * closing the real resource when the count reaches zero.
     * It may be useful to proxy the real resource such that
     * the exposed close method calls this method.
     *
     * @param ignored an ignored resource reference, as required by {@link ThrowingConsumer}
     * @throws IOException as thrown by {@literal e.g.} the resource {@link Closeable#close()} method.
     */
    protected final void decrementUsage(final T ignored) throws IOException {
        final Sink<IOException> closeThrew = sink();
        maybeRef()
                .map(CountAndRef::decrementedZero)
                .map(CountAndRef::getResource)
                .ifPresent(sinking((ThrowingConsumer<T, IOException>) this::internalDispose, closeThrew));
        closeThrew.raise();
    }

    /**
     * Creates a new thread-local for static use by implementations.
     *
     * @param <T> the resource type.
     * @return a new, empty thread-local
     */
    @ReuseResults
    public static <T extends Closeable> ThreadLocal<CountAndRef<T>> newLocalCount() {
        return new ThreadLocal<>();
    }

    /**
     * Returns a consistent reference to a compatible thread-local,
     * which should be statically defined.
     *
     * @return the thread-local
     */
    protected abstract ThreadLocal<CountAndRef<T>> countAndRef();

    /**
     * Creates the actual, real resource and returns the reference.
     *
     * @return the real resource
     */
    @ReuseResults
    protected abstract T createResource();

    /**
     * Returns a reference to the open resource for use,
     * without incrementing the count.
     *
     * @return the resource.
     */
    public abstract T getResource();

    /**
     * Called when the last reference is closed,
     * to dispose of the real resource.
     *
     * @param resource the resource
     * @throws IOException as thrown by {@literal e.g.} {@link Closeable#close}
     */
    protected abstract void disposeResource(final T resource) throws IOException;

    /**
     * Holds the resource and count while a resource is in use.
     *
     * @param <T> the resource type
     */
    protected static final class CountAndRef<T> {
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
