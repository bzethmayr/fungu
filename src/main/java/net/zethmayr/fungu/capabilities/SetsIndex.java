package net.zethmayr.fungu.capabilities;

import net.zethmayr.fungu.fields.Sets;
import net.zethmayr.fungu.fields.SetsX;

import java.util.Optional;

import static java.lang.Integer.parseInt;

public interface SetsIndex extends SetsX {

    @Sets
    void setIndex(final Integer index);

    default void setIndex(final String index) {
        setIndex(Optional.ofNullable(index)
                .map(Integer::parseInt)
                .orElse(null)
        );
    }
}
