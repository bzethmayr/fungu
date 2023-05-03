package net.zethmayr.fungu.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;
import java.util.function.Supplier;

import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static net.zethmayr.fungu.core.SupplierFactory.like;
import static net.zethmayr.fungu.core.ValueFactory.values;

/**
 * Provides exception factories in the broad family {@code becauseX}
 * and exception suppliers in related families {@code xBecause}.
 */
public final class ExceptionFactory {

    private static final String NOT_INSTANTIABLE_F = "This class%scannot be instantiated.";
    private static final String AND_F = " %s and ";

    /**
     * The expected message when instantiating
     * a miscellaneously non-instantiable class.
     */
    public static final String NOT_INSTANTIABLE = notInstantiableAndReason(null);

    /**
     * The expected message when instantiating
     * a class with only static members
     */
    public static final String STATICS_ONLY = notInstantiableAndReason("has only static members");

    /**
     * The expected message when instantiating
     * a class that only declares constants.
     */
    public static final String CONSTANTS_ONLY = notInstantiableAndReason("only defines constants");

    /**
     * The expected message when instantiating
     * a class that only creates adapters.
     */
    public static final String ADAPTERS_ONLY = notInstantiableAndReason("only supplies adapter methods");

    /**
     * The expected message when instantiating
     * a static factory class.
     */
    public static final String FACTORY_METHODS_ONLY = notInstantiableAndReason("only supplies factory methods");

    /**
     * A message format for key uniqueness constraint violations.
     */
    public static final String UNIQUENESS_CONSTRAINT_F = "Uniqueness is required here, but %s is already present.";

    /**
     * A message format for content conflicts.
     */
    public static final String CONFLICT_F = "For the scope %s, the prior value %s conflicts with the proposed %s.";

    /**
     * Provides exceptions
     * indicating illegal arguments.
     *
     * @param argumentConstraint a message or format string.
     * @param illegalValues      any format string values.
     * @return an unchecked exception.
     */
    @NotNull
    public static IllegalArgumentException becauseIllegal(
            @NotNull final String argumentConstraint, final Object... illegalValues
    ) {
        return new IllegalArgumentException(format(argumentConstraint, illegalValues));
    }

    /**
     * Provides exceptions
     * wrapping exceptions thrown
     * indicating illegal arguments.
     *
     * @param argumentConstraint a message or format string
     * @param thrown             a thrown exception
     * @param illegalValues      any format string values
     * @return an unchecked exception
     */
    @NotNull
    public static IllegalArgumentException becauseThrewBecauseIllegal(
            @NotNull final String argumentConstraint, @NotNull final Exception thrown, final Object... illegalValues
    ) {
        return new IllegalArgumentException(format(argumentConstraint, illegalValues), thrown);
    }

    /**
     * Provides exceptions
     * indicating states that should be impossible to reach.
     *
     * @param stateConstraint a message or format string
     * @param violations      any format string values
     * @return an unchecked exception
     */
    @NotNull
    public static IllegalStateException becauseImpossible(
            @NotNull final String stateConstraint, final Object... violations
    ) {
        return new IllegalStateException(format(stateConstraint, violations));
    }

    /**
     * Provides exceptions
     * wrapping exceptions thrown
     * indicating states that should be impossible to reach.
     *
     * @param argumentConstraint a message or format string
     * @param thrown             a thrown exception
     * @param supports           any format string values
     * @return an unchecked exception
     */
    @NotNull
    public static IllegalStateException becauseThrewImpossibly(
            @NotNull final String argumentConstraint, @NotNull final Exception thrown, final Object... supports
    ) {
        return new IllegalStateException(format(argumentConstraint, supports), thrown);
    }

    /**
     * Provides exception suppliers
     * indicating illegal arguments.
     *
     * @param argumentConstraint a message or format string
     * @param valueSuppliers     any format string value suppliers
     * @return an unchecked exception supplier
     */
    @NotNull
    @SafeVarargs
    public static Supplier<IllegalArgumentException> illegalBecause(
            @NotNull final String argumentConstraint, final Supplier<Object>... valueSuppliers
    ) {
        return () -> becauseIllegal(argumentConstraint, values(valueSuppliers));
    }

    /**
     * Provides exceptions
     * indicating access to nonexistent elements
     * of collection-like items.
     *
     * @param keyConstraint a message or format string
     * @param keys          any format string values
     * @return an unchecked exception
     */
    @NotNull
    public static NoSuchElementException becauseNonexistent(
            @NotNull final String keyConstraint, @NotNull final Object... keys
    ) {
        return new NoSuchElementException(format(keyConstraint, keys));
    }

    /**
     * Provides exception suppliers
     * indicating access to nonexistent elements
     * of collection-like items.
     *
     * @param keyConstraint a message or format string
     * @param keySuppliers  any format string value suppliers.
     * @return an unchecked exception supplier
     */
    @NotNull
    @SafeVarargs
    public static Supplier<NoSuchElementException> nonexistentBecause(
            @NotNull final String keyConstraint, final Supplier<Object>... keySuppliers
    ) {
        return () -> becauseNonexistent(keyConstraint, values(keySuppliers));
    }

    /**
     * Provides exceptions
     * indicating key uniqueness constraint violations.
     *
     * @param repeatedKey the repeated key.
     * @return an unchecked exception.
     */
    @NotNull
    public static IllegalArgumentException becauseNotUnique(@Nullable final Object repeatedKey) {
        return becauseIllegal(UNIQUENESS_CONSTRAINT_F, repeatedKey);
    }

    /**
     * Provides exception suppliers
     * indicating key uniqueness constraint violations.
     *
     * @param keySupplier the repeated key supplier
     * @return an unchecked exception supplier.
     */
    public static Supplier<IllegalArgumentException> notUniqueBecause(@NotNull final Supplier<Object> keySupplier) {
        return illegalBecause(UNIQUENESS_CONSTRAINT_F, keySupplier);
    }

    /**
     * Provides exceptions
     * indicating content conflicts.
     *
     * @param scope    the scope of conflict.
     * @param prior    any existing value
     * @param proposed any proposed value
     * @param <T>      the common value type.
     * @return an unchecked exception
     */
    @NotNull
    public static <T> IllegalArgumentException becauseConflicting(final Object scope, final T prior, final T proposed) {
        return becauseIllegal(CONFLICT_F, scope, prior, proposed);
    }

    /**
     * Provides exception suppliers
     * indicating content conflicts.
     *
     * @param scope            supplies scope of conflict.
     * @param suppliesPrior    supplies any existing value.
     * @param suppliesProposed supplies any proposed value.
     * @param <T>              the common value type.
     * @return an unchecked exception supplier.
     */
    public static <T> Supplier<IllegalArgumentException> conflictingBecause(
            final Supplier<Object> scope, final Supplier<T> suppliesPrior, final Supplier<T> suppliesProposed
    ) {
        return illegalBecause(CONFLICT_F, scope, like(suppliesPrior), like(suppliesProposed));
    }

    @NotNull
    private static String notInstantiableAndReason(@Nullable final String specificReason) {
        return nonNull(specificReason)
                ? format(format(NOT_INSTANTIABLE_F, AND_F), specificReason)
                : format(NOT_INSTANTIABLE_F, " ");
    }

    /**
     * Provides exceptions
     * indicating unsupported operations.
     *
     * @param format  a message or format string.
     * @param reasons any format string values.
     * @return an unchecked exception.
     */
    public static UnsupportedOperationException becauseUnsupported(@NotNull final String format, final Object... reasons) {
        return new UnsupportedOperationException(format(format, reasons));
    }

    /**
     * Provides exceptions suppliers
     * indicating unsupported operations.
     *
     * @param format  a message or format string.
     * @param reasons any format string value suppliers.
     * @return an unchecked exception supplier.
     */
    public static Supplier<UnsupportedOperationException> unsupportedBecause(@NotNull final String format, final Supplier<Object>... reasons) {
        return () -> becauseUnsupported(format, values(reasons));
    }

    /**
     * Returns an exception
     * indicating that instantiation is not supported.
     *
     * @return an unchecked exception.
     */
    public static UnsupportedOperationException becauseNotInstantiable() {
        return becauseUnsupported(NOT_INSTANTIABLE);
    }

    /**
     * Returns an exception
     * indicating that instantiation is not supported
     * because a class only declares static members.
     *
     * @return an unchecked exception.
     */
    public static UnsupportedOperationException becauseStaticsOnly() {

        return becauseUnsupported(STATICS_ONLY);
    }

    /**
     * Returns an exception
     * indicating that instantiation is not supported
     * because a class only declares constants.
     *
     * @return an unchecked exception.
     */
    public static UnsupportedOperationException becauseConstantsOnly() {
        return becauseUnsupported(CONSTANTS_ONLY);
    }

    /**
     * Returns an exception
     * indicating that instantiation is not supported
     * because a class only provides adapters.
     *
     * @return an unchecked exception.
     */
    public static UnsupportedOperationException becauseAdaptersOnly() {
        return becauseUnsupported(ADAPTERS_ONLY);
    }

    /**
     * Returns an exception
     * indicating that instantiation is not supported
     * for a static factory class.
     *
     * @return an unchecked exception.
     */
    public static UnsupportedOperationException becauseFactory() {
        return becauseUnsupported(FACTORY_METHODS_ONLY);
    }

    private ExceptionFactory() {
        throw becauseStaticsOnly();
    }
}
