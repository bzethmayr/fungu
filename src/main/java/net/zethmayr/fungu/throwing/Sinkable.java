package net.zethmayr.fungu.throwing;

import java.util.function.Consumer;

/**
 * A sinkable functional interface has
 * a method that throws checked exceptions and
 * can be converted to a non-throwing functional interface
 * by providing an exception sink.
 *
 * @param <S> the non-throwing functional interface.
 */
public interface Sinkable<S> {

    /**
     * Returns an analogue which
     * does not throw (checked) exceptions and
     * may capture any exception thrown.
     *
     * @param sink an exception consumer.
     * @return a non-throwing analogue.
     */
    S sinking(final Consumer<Exception> sink);

    /**
     * Given a sinkable implementation and a sink,
     * returns a non-throwing analogue.
     *
     * @param throwing a sinkable implementation.
     * @param sink     an exception consumer.
     * @param <S>      the non-throwing functional interface.
     * @return a non-throwing analogue.
     */
    static <S> S sinking(final Sinkable<S> throwing, final Consumer<Exception> sink) {
        return throwing.sinking(sink);
    }
}
