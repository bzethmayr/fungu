package net.zethmayr.fungu;

import net.zethmayr.fungu.throwing.ThrowingConsumer;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import static net.zethmayr.fungu.core.ExceptionFactory.becauseStaticsOnly;

/**
 * Provides methods that implement the chained mutator and visitor patterns.
 */
public final class UponHelper {

    private UponHelper() {
        throw becauseStaticsOnly();
    }

    /**
     * Provides chained mutator syntax for any mutable class, by
     * returning the given target after visiting it with all given consumers.
     *
     * @param target  a target instance.
     * @param changes any changes.
     * @param <T>     the target type.
     * @return the instance passed.
     */
    @SafeVarargs
    public static <T> T upon(
            final T target, final Consumer<? super T>... changes
    ) {
        for (final Consumer<? super T> change : changes) {
            change.accept(target);
        }
        return target;
    }

    /**
     * Returns an operator
     * that applies the given changes
     * and returns the instance passed.
     *
     * @param changes any changes.
     * @param <T>     the common type.
     * @return a mutative identity operator.
     * @see #upon(Object, Consumer[])
     */
    @SafeVarargs
    public static <T> UnaryOperator<T> with(
            final Consumer<? super T>... changes
    ) {
        return t -> upon(t, changes);
    }

    /**
     * Provides chained mutator syntax over void methods that throw checked exceptions.
     * Returns the target after visiting it with all given consumers.
     *
     * @param target  a target instance.
     * @param changes any changes.
     * @param <T>     the target type.
     * @param <E>     the common exception type.
     * @return the instance passed
     * @throws E when changes fail.
     */
    @SafeVarargs
    public static <T, E extends Exception> T throwingUpon(
            final T target, final ThrowingConsumer<? super T, E>... changes
    ) throws E {
        for (final ThrowingConsumer<? super T, E> change : changes) {
            change.accept(target);
        }
        return target;
    }
}
