package net.zethmayr.fungu.arc;

import net.zethmayr.fungu.declarations.SingleUse;
import net.zethmayr.fungu.throwing.ThrowingConsumer;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static net.zethmayr.fungu.CloseableFactory.closeIntercepted;
import static net.zethmayr.fungu.core.ExceptionFactory.becauseUnsupported;

/**
 * This counted reference
 * is decremented when the resource is closed,
 * to prevent the resource being closed without the count being decremented.
 * Calling code obtains resources via the {@code openTransparent} methods
 * instead of by constructing subclass instances directly:
 * <code>
 * try (final Connection cxn = openTransparent(RecursiveConnection::new) {
 *     // cxn uses
 * }
 * </code>
 * If calling code closes the resource before the try-with-resources construct would have done so,
 * subsequent uses will fail visibly.
 * @param <R> the resource type.
 */
public abstract class TransparentCountedReference<R extends Closeable> extends SimpleCountedReference<R> {

    private final R closeProtected;

    /**
     * Creates an open counted reference to the resource and increments the count.
     * Calling code should never use this instance
     * as the target of try-with-resources constructs;
     * use {@link #openTransparent(Supplier)}/{@link #openTransparent(BiFunction, Class, Class[])} instead.
     *
     * @param resourceInterface    the primary resource interface
     * @param additionalInterfaces any additional supported interfaces.
     */
    protected TransparentCountedReference(final Class<R> resourceInterface, final Class<?>... additionalInterfaces) {
        super();
        closeProtected = closeIntercepted(resourceInterface, super.getResource(), (ThrowingConsumer<R, IOException>) this::decrementUsage, additionalInterfaces);
    }

    /**
     * Returns a reference to the open resource for use,
     * without incrementing the count.
     * Calling code needs to call {@link Closeable#close()} on the resource instance
     * when done using it.
     * The resource will not actually be closed until all references are closed.
     * @return the open, closeable resource.
     */
    @Override
    public R getResource() {
        return closeProtected;
    }

    /**
     * {@inheritDoc}
     * This implementation instead throws a runtime exception.
     * Calling code should not have direct access to this instance.
     */
    @Override
    public void close() {
        throw becauseUnsupported("Close the resource instead.");
    }

    /**
     * Opens a counted reference to a well-known resource type,
     * using a specific counted reference constructor and arguments.
     * Calling code should close this reference when done with it.
     * @param ctor a method that will create a transient, transparent counted reference.
     * @param resourceInterface the primary resource interface.
     * @param additionalInterfaces any additional supported interface.
     * @return an open, closeable resource reference.
     * @param <R> the resource type.
     * @param <T> the transparent counted reference type.
     */
    public static <R extends Closeable, T extends TransparentCountedReference<R>> R openTransparent(
            final BiFunction<Class<R>, Class<?>[], T> ctor, final Class<R> resourceInterface, final Class<?>... additionalInterfaces
    ) {
        return ctor.apply(resourceInterface, additionalInterfaces).getResource();
    }

    /**
     * Opens a counted reference to a well-known resource type,
     * using the provided default constructor or supplier.
     * Calling code should close this reference when done with it.
     * @param opensCountedReference the counted reference constructor
     * @return an open, closeable resource reference.
     * @param <R> the resource type.
     * @param <T> the transparent counted reference type.
     */
    @SingleUse
    public static <R extends Closeable, T extends TransparentCountedReference<R>> R openTransparent(
            final Supplier<T> opensCountedReference
    ) {
        return opensCountedReference.get().getResource();
    }
}
