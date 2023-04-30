package net.zethmayr.fungu;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.zethmayr.fungu.core.ExceptionFactory.becauseAdaptersOnly;

/**
 * Various methods adapting down to {@link Consumer}.
 */
public final class ConsumerFactory {
    private ConsumerFactory() {
        throw becauseAdaptersOnly();
    }

    /**
     * Explicitly anchors a consumer reference.
     *
     * @param duckConsumer a sufficiently consumer-like method
     * @param <T>          the consumed type
     * @return the method passed, as a consumer
     */
    public static <T> Consumer<T> consumer(final Consumer<? super T> duckConsumer) {
        return duckConsumer::accept;
    }

    /**
     * Returns a consumer that does nothing.
     *
     * @param <T> the type done nothing with.
     * @return a consumer.
     */
    public static <T> Consumer<T> nothing() {
        return x -> {
        };
    }

    /**
     * Adapts a function (presumably with side effects) to a consumer that
     * ignores the function return value.
     *
     * @param baseFunction a function whose return value can be ignored
     * @param <T>          the function argument type
     * @param <R>          the ignored result type
     * @return a consumer calling the function
     */
    public static <T, R> Consumer<T> ignoringResult(final Function<? super T, R> baseFunction) {
        return baseFunction::apply;
    }

    /**
     * Adapts a function to a consumer that
     * passes the value to the function and the result to the sink.
     *
     * @param baseFunction a function whose return value is redirected
     * @param resultSink   the result consumer
     * @param <T>          the argument type
     * @param <R>          the result type
     * @return a consumer calling the function
     */
    public static <T, R> Consumer<T> redirectingResult(
            final Function<? super T, R> baseFunction, final Consumer<? super R> resultSink
    ) {
        return t -> resultSink.accept(baseFunction.apply(t));
    }

    /**
     * Adapts a bi-consumer to a consumer by
     * fixing the first argument.
     *
     * @param both        the bi-consumer being adapted
     * @param prefixValue the fixed first argument value
     * @param <T>         the first argument type
     * @param <R>         the second argument type
     * @return a consumer taking the second argument
     */
    public static <T, R> Consumer<R> prefixingWith(final BiConsumer<T, R> both, final T prefixValue) {
        return prefixingFrom(both, () -> prefixValue);
    }

    /**
     * Adapts a bi-consumer to a consumer by
     * fixing the second argument.
     *
     * @param both        the bi-consumer being adapted
     * @param suffixValue the fixed second argument value
     * @param <T>         the first argument type
     * @param <R>         the second argument type
     * @return a consumer taking the first argument
     */
    public static <T, R> Consumer<T> suffixingWith(final BiConsumer<T, R> both, final R suffixValue) {
        return suffixingFrom(both, () -> suffixValue);
    }

    /**
     * Adapts a bi-consumer to a consumer by
     * supplying the first argument from a predetermined source.
     *
     * @param both         the bi-consumer being adapted
     * @param prefixSource the first argument supplier
     * @param <T>          the first argument type
     * @param <R>          the second argument type
     * @return a consumer taking the second argument
     */
    public static <T, R> Consumer<R> prefixingFrom(final BiConsumer<T, R> both, final Supplier<T> prefixSource) {
        return r -> both.accept(prefixSource.get(), r);
    }

    /**
     * Adapts a bi-consumer to a consumer by
     * supplying the second argument from a predetermined source.
     *
     * @param both         the bi-consumer being adapted
     * @param suffixSource the second argument supplier
     * @param <T>          the first argument type
     * @param <R>          the second argument type
     * @return a consumer taking the first argument
     */
    public static <T, R> Consumer<T> suffixingFrom(final BiConsumer<T, R> both, final Supplier<R> suffixSource) {
        return t -> both.accept(t, suffixSource.get());
    }
}
