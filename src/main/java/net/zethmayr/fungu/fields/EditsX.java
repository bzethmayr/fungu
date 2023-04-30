package net.zethmayr.fungu.fields;

import net.zethmayr.fungu.core.ExceptionFactory;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.*;

/**
 * Provides facilities for copying field values.
 */
public interface EditsX extends HasX, SetsX {

    /**
     * Returns a visitor that
     * applies the copier function,
     * taking the value from this instance
     * and providing it to the consumed instance.
     *
     * @param editable   the interface to be mutated.
     * @param fieldClass the interface providing the value.
     * @param <E>        the edited interface
     * @param <T>        the field value interface.
     * @return a visitor that copies values from this instance.
     */
    default <E extends EditsX, T> Consumer<E> getFieldCopier(
            final Class<E> editable, final Class<T> fieldClass
    ) {
        // safety checks? maybe...
        final BiConsumer<E, E> copierFunction = getCopyFunction(editable, fieldClass);
        return e -> copierFunction.accept(editable.cast(this), e);
    }

    static <E extends EditsX, T> BiConsumer<E, E> getCopyFunction(
            final Class<E> editable, final Class<T> fieldType
    ) {
        return CopierWiring.INSTANCE.getCopyFunction(editable);
    }

    static <E extends EditsX, T> void registerCopyFunction(
            final Class<E> editable, final Supplier<BiConsumer<E, E>> copierGenerator
    ) {
        CopierWiring.INSTANCE.registerCopyFunction(editable, copierGenerator);
    }


    enum CopierWiring {
        INSTANCE;

        private final ConcurrentMap<Class<?>, BiConsumer<? extends EditsX, ? extends EditsX>> copiers =
                new ConcurrentHashMap<>();

        <E extends EditsX> BiConsumer<E, E> getCopyFunction(
                final Class<E> declaring
        ) {
            return  Optional.ofNullable((BiConsumer<E, E>) copiers.get(declaring))
                    .orElseThrow(ExceptionFactory.unsupportedBecause("no copier for %s in %s", declaring, copiers));
        }

        <E extends EditsX> void registerCopyFunction(
                final Class<E> editable,
                final Supplier<BiConsumer<E, E>> functionGenerator
        ) {
            copiers.computeIfAbsent(editable, k -> functionGenerator.get());
        }
    }
}
