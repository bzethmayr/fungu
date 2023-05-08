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

    /**
     * Returns a consumer which will set this instance's field value to the value accepted.
     * @param setting the settable interface.
     * @param fieldClass the value type.
     * @return a value consumer setting this instance's field.
     * @param <S> the settable type.
     * @param <T> the value type.
     */
    default <S extends SetsX, T> Consumer<T> getSetter(
            final Class<S> setting, final Class<T> fieldClass
    ) {
        final BiConsumer<S, T> setFunction = getSetFunction(setting, fieldClass);
        return v -> setFunction.accept(setting.cast(this), v);
    }

    /**
     * Registers unregistered setter methods from the generator given.
     *
     * @param setting the settable interface.
     * @param setterGenerator a setter method generator.
     * @param <S> the settable type.
     * @param <T> the value type.
     */
    static <S extends SetsX, T> void registerSetFunction(
            final Class<S> setting, final Supplier<BiConsumer<S, T>> setterGenerator
    ) {
        SetterWiring.INSTANCE.registerSetFunction(setting, setterGenerator);
    }

    /**
     * Returns the registered setter method
     * for the given settable interface.
     * @param setting the settable interface.
     * @param fieldType the field type (optional).
     * @return a setter method.
     * @param <S> the settable type.
     * @param <T> the field type.
     */
    static <S extends SetsX, T> BiConsumer<S, T> getSetFunction(
            final Class<S> setting, final Class<T> fieldType
    ) {
        return SetterWiring.INSTANCE.getSetFunction(setting);
    }

    /**
     * Returns the type-erased registered setter method
     * for the given settable interface.
     * @param setting the settable interface.
     * @return a type-erased setter method.
     */
    static BiConsumer<?, ?> getSetFunction(
            final Class<? extends SetsX> setting
    ) {
        return SetterWiring.INSTANCE.getSetFunction(setting);
    }

    /**
     * Registry of field settable interfaces and setters.
     */
    enum SetterWiring {
        /**
         * The sole setter registrar.
         */
        INSTANCE;

        private final ConcurrentMap<Class<? extends SetsX>, BiConsumer<? extends SetsX, ?>> setters =
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
