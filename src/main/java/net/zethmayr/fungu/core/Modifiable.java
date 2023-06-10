package net.zethmayr.fungu.core;

import java.util.function.Consumer;

import static net.zethmayr.fungu.UponHelper.upon;

/**
 * This bolt-on capability for mutable classes
 * allows them to accept arbitrarily declared mutations
 * along with fluent field mutators.
 *
 * @param <T> the mutable type.
 */
public interface Modifiable<T extends Modifiable<T>> {

    /**
     * Applies the given consumer to this instance,
     * returning this instance.
     * @param changes A modifying consumer.
     * @return the same instance.
     */
    default T modified(final Consumer<? super T> changes) {
        return upon((T) this, changes);
    }
}
