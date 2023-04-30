package net.zethmayr.fungu.core;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;
import java.util.function.Supplier;

import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static net.zethmayr.fungu.core.SupplierFactory.like;
import static net.zethmayr.fungu.core.ValueFactory.values;

public final class ExceptionFactory {

    private static final String NOT_INSTANTIABLE_F = "This class%scannot be instantiated.";
    private static final String AND_F = " %s and ";

    public static final String NOT_INSTANTIABLE = notInstantiableAndReason(null);
    public static final String STATICS_ONLY = notInstantiableAndReason("has only static members");
    public static final String CONSTANTS_ONLY = notInstantiableAndReason("only defines constants");

    public static final String ADAPTERS_ONLY = notInstantiableAndReason("only supplies adapter methods");

    public static final String FACTORY_METHODS_ONLY = notInstantiableAndReason("only supplies factory methods");

    public static final String UNIQUENESS_CONSTRAINT_F = "Uniqueness is required here, but %s is already present.";

    public static final String CONFLICT_F = "For the scope %s, the prior value %s conflicts with the proposed %s.";

    @NotNull
    public static IllegalArgumentException becauseIllegal(
            @NotNull final String argumentConstraint, final Object... illegalValues
    ) {
        return new IllegalArgumentException(format(argumentConstraint, illegalValues));
    }

    @NotNull
    public static IllegalArgumentException becauseThrewBecauseIllegal(
            @NotNull final String argumentConstraint, @NotNull final Exception thrown, final Object... illegalValues
    ) {
        return new IllegalArgumentException(format(argumentConstraint, illegalValues), thrown);
    }

    @NotNull
    public static IllegalStateException becauseImpossible(
            @NotNull final String stateConstraint, final Object... violations
    ) {
        return new IllegalStateException(format(stateConstraint, violations));
    }

    @NotNull
    public static IllegalStateException becauseThrewImpossibly(
            @NotNull final String argumentConstraint, @NotNull final Exception thrown, final Object... supports
    ) {
        return new IllegalStateException(format(argumentConstraint, supports), thrown);
    }

    @NotNull
    @SafeVarargs
    public static Supplier<IllegalArgumentException> illegalBecause(
            @NotNull final String argumentConstraint, final Supplier<Object>... valueSuppliers
    ) {
        return () -> becauseIllegal(argumentConstraint, values(valueSuppliers));
    }

    @NotNull
    public static NoSuchElementException becauseNonexistent(
            @NotNull final String keyConstraint, @NotNull final Object... keys
    ) {
        return new NoSuchElementException(format(keyConstraint, keys));
    }

    @NotNull
    @SafeVarargs
    public static Supplier<NoSuchElementException> nonexistentBecause(
            @NotNull final String keyConstraint, final Supplier<Object>... keySuppliers
    ) {
        return () -> becauseNonexistent(keyConstraint, values(keySuppliers));
    }

    @NotNull
    public static IllegalArgumentException becauseNotUnique(@Nullable final Object valueSuppliers) {
        return becauseIllegal(UNIQUENESS_CONSTRAINT_F, valueSuppliers);
    }

    public static Supplier<IllegalArgumentException> notUniqueBecause(@NotNull final Supplier<Object> repeatedValue) {
        return illegalBecause(UNIQUENESS_CONSTRAINT_F, repeatedValue);
    }

    @NotNull
    public static <T> IllegalArgumentException becauseConflicting(final Object scope, final T prior, final T proposed) {
        return becauseIllegal(CONFLICT_F, scope, prior, proposed);
    }

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

    public static UnsupportedOperationException becauseUnsupported(@NotNull final String reason) {
        return new UnsupportedOperationException(reason);
    }

    public static Supplier<UnsupportedOperationException> unsupportedBecause(@NotNull final String format, final Object... reasons) {
        return () -> becauseUnsupported(format(format, reasons));
    }

    public static UnsupportedOperationException becauseNotInstantiable() {
        return becauseUnsupported(NOT_INSTANTIABLE);
    }

    public static UnsupportedOperationException becauseStaticsOnly() {

        return becauseUnsupported(STATICS_ONLY);
    }

    public static UnsupportedOperationException becauseConstantsOnly() {
        return becauseUnsupported(CONSTANTS_ONLY);
    }

    public static UnsupportedOperationException becauseAdaptersOnly() {
        return becauseUnsupported(ADAPTERS_ONLY);
    }

    public static UnsupportedOperationException becauseFactory() {
        return becauseUnsupported(FACTORY_METHODS_ONLY);
    }

    private ExceptionFactory() {
        throw becauseStaticsOnly();
    }
}
