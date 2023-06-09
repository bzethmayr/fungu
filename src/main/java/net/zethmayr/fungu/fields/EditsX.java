package net.zethmayr.fungu.fields;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.zethmayr.fungu.core.ExceptionFactory.becauseUnsupported;

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
     * @param editable   the edited interface.
     * @param fieldClass the interface providing the value.
     * @param <E>        the edited type.
     * @param <T>        the field value type.
     * @return a visitor that copies values from this instance.
     */
    default <E extends EditsX, T> Consumer<E> getFieldCopier(
            final Class<E> editable, final Class<T> fieldClass
    ) {
        // safety checks? maybe...
        final BiConsumer<E, E> copierFunction = getCopyFunction(editable, fieldClass);
        return e -> copierFunction.accept(editable.cast(this), e);
    }

    /**
     * Returns a visitor that
     * applies the copier function,
     * taking all values from this instance
     * and providing them to the consumed instance.
     *
     * @param editable the edited interface.
     * @param <E>      the edited type.
     * @return a visitor that copies values from this instance.
     */
    default <E extends EditsX> Consumer<E> getCopier(final Class<E> editable) {
        return getFieldCopier(editable, null);
    }

    /**
     * Returns a copier for a given editable scope.
     *
     * @param editable  the editable type.
     * @param fieldType the (optional) field type.
     * @param <E>       the editable type.
     * @param <T>       the (optional) field type.
     * @return a bi-consumer copying fields from the first to the second argument.
     */
    static <E extends EditsX, T> BiConsumer<E, E> getCopyFunction(
            final Class<E> editable, final Class<T> fieldType
    ) {
        return CopierWiring.INSTANCE.getCopyFunction(editable);
    }

    /**
     * Runs the provided generator to register an editable class's copier,
     * if none is already present.
     *
     * @param editable        the editable class.
     * @param copierGenerator a copier generating method.
     * @param <E>             the editable type.
     * @param <T>             the (optional) field type.
     */
    static <E extends EditsX, T> void registerCopyFunction(
            final Class<E> editable, final Supplier<BiConsumer<E, E>> copierGenerator
    ) {
        CopierWiring.INSTANCE.registerCopyFunction(editable, copierGenerator);
    }

    /**
     * Registrar for editable class copiers.
     */
    enum CopierWiring {
        /**
         * Singleton instance.
         */
        INSTANCE;

        private final ConcurrentMap<Class<?>, BiConsumer<? extends EditsX, ? extends EditsX>> copiers =
                new ConcurrentHashMap<>();

        <E extends EditsX> BiConsumer<E, E> getCopyFunction(
                final Class<E> declaring
        ) {
            return Optional.ofNullable((BiConsumer<E, E>) copiers.get(declaring))
                    .orElseThrow(() -> becauseUnsupported("no copier for %s in %s", declaring, copiers));
        }

        <E extends EditsX> void registerCopyFunction(
                final Class<E> editable,
                final Supplier<BiConsumer<E, E>> functionGenerator
        ) {
            copiers.computeIfAbsent(editable, k -> functionGenerator.get());
        }
    }
}
