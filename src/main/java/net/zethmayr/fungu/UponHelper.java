package net.zethmayr.fungu;

import net.zethmayr.fungu.throwing.ThrowingConsumer;

import java.util.function.Consumer;

import static net.zethmayr.fungu.core.ExceptionFactory.becauseStaticsOnly;

/**
 * Provides methods that implement the visitor pattern.
 */
public final class UponHelper {

    private UponHelper() {
        throw becauseStaticsOnly();
    }

    /**
     * Returns the target after visiting it with all given consumers.
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
