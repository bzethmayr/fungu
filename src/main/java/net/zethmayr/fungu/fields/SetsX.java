package net.zethmayr.fungu.fields;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.zethmayr.fungu.core.ExceptionFactory.becauseUnsupported;

/**
 * Meta-interface for interfaces defining field setters.
 * Provides access to the setter method(s).
 */
public interface SetsX {

    default <S extends SetsX, T> Consumer<T> getSetter(
            final Class<S> setting, final Class<T> fieldClass
    ) {
        final BiConsumer<S, T> setFunction = getSetFunction(setting, fieldClass);
        return v -> setFunction.accept(setting.cast(this), v);
    }

    static <S extends SetsX, T> void registerSetFunction(
            final Class<S> setting, final Supplier<BiConsumer<S, T>> setterGenerator
    ) {
        SetterWiring.INSTANCE.registerSetFunction(setting, setterGenerator);
    }

    static <S extends SetsX, T> BiConsumer<S, T> getSetFunction(
            final Class<S> setting, final Class<T> fieldType
    ) {
        return SetterWiring.INSTANCE.getSetFunction(setting);
    }

    static BiConsumer<?, ?> getSetFunction(
            final Class<? extends SetsX> setting
    ) {
        return SetterWiring.INSTANCE.getSetFunction(setting);
    }

    enum SetterWiring {
        INSTANCE;

        final ConcurrentMap<Class<? extends SetsX>, BiConsumer<? extends SetsX, ?>> setters =
                new ConcurrentHashMap<>();

        <S extends SetsX, T> void registerSetFunction(
                final Class<S> declaring, final Supplier<BiConsumer<S, T>> setterGenerator
        ) {
            setters.computeIfAbsent(declaring, k -> setterGenerator.get());
        }

        <S extends SetsX, T> BiConsumer<S, T> getSetFunction(
                final Class<S> declaring
        ) {
            return Optional.ofNullable((BiConsumer<S, T>) setters.get(declaring))
                    .orElseThrow(() -> becauseUnsupported("no setter for %s in %s", declaring, setters));
        }
    }
}
