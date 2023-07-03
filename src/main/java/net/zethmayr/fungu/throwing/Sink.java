package net.zethmayr.fungu.throwing;

import java.util.function.Consumer;

/**
 * Defers an exception to allow functional composition.
 */
public interface Sink<E extends Exception> extends Consumer<E>, Revenant<E, Sink<E>> {

    /**
     * Receives exceptions from sinkable implementations.
     *
     * @param thrown an exception.
     */
    @Override
    void accept(final E thrown);
}
