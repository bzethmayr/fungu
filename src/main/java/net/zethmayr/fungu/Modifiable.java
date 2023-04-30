package net.zethmayr.fungu;

import java.util.function.Consumer;

import static net.zethmayr.fungu.UponHelper.upon;

public interface Modifiable<T extends Modifiable<T>> {

    default T modified(final Consumer<? super T> changes) {
        return upon((T) this, changes);
    }
}
