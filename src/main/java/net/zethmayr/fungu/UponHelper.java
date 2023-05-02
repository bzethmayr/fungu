package net.zethmayr.fungu;

import net.zethmayr.fungu.core.ExceptionFactory;
import net.zethmayr.fungu.throwing.ThrowingConsumer;

import java.util.function.Consumer;

public final class UponHelper {

    private UponHelper() {
        throw ExceptionFactory.becauseStaticsOnly();
    }

    @SafeVarargs
    public static <T> T upon(
            final T target, final Consumer<? super T>... changes
    ) {
        for (final Consumer<? super T> change : changes) {
            change.accept(target);
        }
        return target;
    }

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
