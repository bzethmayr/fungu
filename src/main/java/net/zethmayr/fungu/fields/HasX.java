package net.zethmayr.fungu.fields;

import net.zethmayr.fungu.core.ExceptionFactory;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Meta-interface for interfaces defining field getters.
 * Provides access to the getter method(s).
 */
public interface HasX {
    /*
     * A readable field is, in abstract, an instantiation of {@link HasX}.
     * <p/>
     * However, HasX's implied accessor method cannot be defined in HasX,
     * since at this level, the requirement is only that a {@literal has}X method is defined,
     * and X is not known.
     * <p/>
     * Also, {@link HasX} cannot be generified,
     * since an implementing instance is expected to expose multiple fields
     * and signatures would clash.
     */

    /**
     * Returns a supplier that
     * returns the field value.
     *
     * @param having     the having interface.
     * @param fieldClass the field type
     * @param <H>        the having interface
     * @param <T>        the field type
     * @return a field supplier
     */
    default <H extends HasX, T> Supplier<T> getGetter(
            final Class<H> having, final Class<T> fieldClass
    ) {
        final Function<H, T> getFunction = getGetFunction(having, fieldClass);
        return () -> getFunction.apply(having.cast(this));
    }

    /**
     * Registers the getter method for a given field interface.
     *
     * @param having the defining interface
     * @param getterGenerator the getter method
     * @param <H>    the defining interface
     * @param <T>    the field type
     */
    static <H extends HasX, T> void registerGetFunction(
            final Class<H> having, final Supplier<Function<H, T>> getterGenerator
    ) {
        GetterWiring.INSTANCE.registerGetFunction(having, getterGenerator);
    }

    /**
     * Returns the function that
     * accesses the interface field.
     *
     * @param declaring the having interface
     * @param fieldType the field type
     * @param <H>       the having interface
     * @param <T>       the field type
     * @return the field accessor function
     */
    static <H extends HasX, T> Function<H, T> getGetFunction(
            final Class<H> declaring, final Class<T> fieldType
    ) {
        return GetterWiring.INSTANCE.getGetFunction(declaring);
    }

    static Function<?, ?> getGetFunction(
            final Class<? extends HasX> declaring
    ) {
        return GetterWiring.INSTANCE.getGetFunction(declaring);
    }

    /**
     * Registry of field value interfaces and getters.
     */
    enum GetterWiring {
        INSTANCE;
        /**
         * Since the number of independently significant fields is finite tending towards small,
         * this permanent map does not create a memory leak.
         */
        final ConcurrentMap<Class<? extends HasX>, Function<? extends HasX, ?>> getters =
                new ConcurrentHashMap<>();

        /**
         * Associates the given declaring interface with a getter method.
         *
         * @param declaring   the field value interface
         * @param getterGenerator the field getter method
         * @param <H>         the interface type
         * @param <T>         the field type
         */
        <H extends HasX, T> void registerGetFunction(
                final Class<H> declaring, final Supplier<Function<H, T>> getterGenerator
        ) {
            getters.computeIfAbsent(declaring, k -> getterGenerator.get());
        }

        /**
         * Returns the getter method registered for the given declaring interface.
         *
         * @param declaring the field value interface
         * @param <H>       the interface type
         * @param <T>       the field type
         * @return the field getter method
         */
        <H extends HasX, T> Function<H, T> getGetFunction(final Class<H> declaring) {
            return Optional.ofNullable((Function<H, T>) getters.get(declaring))
                    .orElseThrow(ExceptionFactory.unsupportedBecause("no getter for %s in %s", declaring, getters));
        }
    }
}
