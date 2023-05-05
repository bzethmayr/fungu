package net.zethmayr.fungu.arc;

import java.io.Closeable;
import java.io.IOException;

import static net.zethmayr.fungu.core.ExceptionFactory.nonexistentBecause;
import static net.zethmayr.fungu.core.SupplierFactory.from;

/**
 * This {@link CountedReference} is based on closeable resources,
 * and has no mechanism to
 * prevent inconsistent resource states due to calling code conventions.
 * This is useful if you can control all attempts to close the resource,
 * since it does not require intercepting any methods.
 *
 * @param <T> the resource type.
 */
public abstract class SimpleCountedReference<T extends Closeable> extends CountedReference<T> {

    /**
     * Creates a new, open, counted reference to the open resource
     * and increments the reference count.
     */
    protected SimpleCountedReference() {
        super();
    }

    /**
     * {@inheritDoc}
     * By default, just calls {@link Closeable#close()} on the resource,
     * per the convention that the resource will account for all of its own disposal.
     * If this is not entirely the case, this method can be overridden.
     *
     * @param resource a resource to dispose of
     * @throws IOException if resource disposal fails.
     */
    @Override
    protected void disposeResource(final T resource) throws IOException {
        resource.close();
    }

    /**
     * {@inheritDoc}
     * Calling code should not call {@link Closeable#close()} on the result directly,
     * as it will break subsequent uses.
     *
     * @return the resource.
     */
    public T getResource() {
        return maybeRef()
                .map(CountAndRef::getResource)
                .orElseThrow(nonexistentBecause("Resource not open in %s", from(this)));
    }


}
