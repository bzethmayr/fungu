package net.zethmayr.fungu.capabilities;

import net.zethmayr.fungu.fields.Sets;
import net.zethmayr.fungu.fields.SetsX;

import java.util.Optional;

import static java.lang.Integer.parseInt;

/**
 * Can receive an index field value.
 */
public interface SetsIndex extends SetsX {

    /**
     * Accepts a new index value.
     * @param index an index value.
     */
    @Sets
    void setIndex(final Integer index);

    /**
     * Accepts a new index value from a string.
     * @param index an index value, as a string.
     */
    default void setIndex(final String index) {
        setIndex(Optional.ofNullable(index)
                .map(Integer::parseInt)
                .orElse(null)
        );
    }
}
