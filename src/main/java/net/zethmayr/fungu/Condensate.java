package net.zethmayr.fungu;

import java.util.Optional;

public interface Condensate<T, E extends Exception> {

    T get();

    E getException();

    default T getOrThrow() throws E {
        return Optional.of(get())
                .orElseThrow(this::getException);
    }

    default Object getResult() {
        return Optional.ofNullable((Object) get())
                .orElseGet(this::getException);
    }
}
