package net.zethmayr.fungu.throwing;

import java.util.function.Consumer;

/**
 * Defers an exception to allow functional composition.
 */
public interface Sink extends Consumer<Exception> {

    /**
     * Receives exceptions from sinkable implementations.
     * @param thrown an exception.
     */
    @Override
    void accept(final Exception thrown);

    /**
     * Throws received exceptions.
     * @throws Exception received from sinkable implementation.
     */
    void raise() throws Exception;
}
