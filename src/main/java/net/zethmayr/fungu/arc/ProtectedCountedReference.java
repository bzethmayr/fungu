package net.zethmayr.fungu.arc;

import java.io.Closeable;

import static net.zethmayr.fungu.CloseableFactory.closeIntercepted;
import static net.zethmayr.fungu.ConsumerFactory.nothing;

/**
 * This counted reference
 * blocks the resource's close method
 * to prevent the resource being closed without the count being decremented.
 * Calling code obtains resources by constructing subclass instances
 * and releases their uses by closing those instances.
 *
 * @param <T> the resource type
 */
public abstract class ProtectedCountedReference<T extends Closeable> extends SimpleCountedReference<T> {

    private final T closeProtected;

    /**
     * Creates an open counted reference to the resource and increments the count.
     * Calling code should always use this instance
     * as the target of try-with-resources constructs.
     *
     * @param resourceInterface    the primary resource interface
     * @param additionalInterfaces any additional supported interfaces.
     */
    protected ProtectedCountedReference(final Class<T> resourceInterface, final Class<?>... additionalInterfaces) {
        super();
        closeProtected = closeIntercepted(resourceInterface, super.getResource(), nothing(), additionalInterfaces);
    }

    /**
     * Returns a reference to the open resource for use,
     * without incrementing the count.
     * Calling {@link Closeable#close()} on this instance does nothing -
     * the resource reference instance must be closed instead.
     *
     * @return the open resource.
     */
    @Override
    public T getResource() {
        return closeProtected;
    }
}
