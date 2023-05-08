package net.zethmayr.fungu;

import net.zethmayr.fungu.core.ExceptionFactory;
import net.zethmayr.fungu.core.SuppressionConstants;

import java.util.function.Predicate;

/**
 * Primitive and composed predicates
 */
public final class PredicateFactory {

    /**
     * A primitive predicate that never returns true.
     */
    public static final Predicate<Object> NEVER = x -> false;
    /**
     * A primitive predicate that always returns true.
     */
    public static final Predicate<Object> ALWAYS = x -> true;

    private PredicateFactory() {
        throw ExceptionFactory.becauseStaticsOnly();
    }

    @SuppressWarnings(SuppressionConstants.CHECK_ASSURED)
    private static <T> Predicate<T> genericDegenerate(final Predicate<Object> degenerate) {
        return (Predicate<T>) degenerate;
    }

    /**
     * Returns a new predicate that
     * requires all the given predicates to return true.
     * In the case that none are provided,
     * the predicate will always pass.
     *
     * @param inclusions the included predicates.
     * @param <T>        the tested type.
     * @return a new predicate requiring all the given predicates to pass.
     */

    @SafeVarargs
    public static <T> Predicate<T> allOf(final Predicate<? super T>... inclusions) {
        Predicate<T> requirement = genericDegenerate(ALWAYS);
        for (Predicate<? super T> further : inclusions) {
            requirement = requirement.and(further);
        }
        return requirement;
    }

    /**
     * Returns a new predicate that
     * requires at least one of the given predicates to return true.
     * In the case that none are provided,
     * the predicate will never pass.
     *
     * @param inclusions the included predicates.
     * @param <T>        the tested type.
     * @return a new predicate requiring at least one of the given predicates to pass.
     */
    @SafeVarargs
    public static <T> Predicate<T> anyOf(final Predicate<? super T>... inclusions) {
        Predicate<T> requirement = genericDegenerate(NEVER);
        for (Predicate<? super T> further : inclusions) {
            requirement = requirement.or(further);
        }
        return requirement;
    }

    /**
     * Returns a new predicate that
     * requires that none of the given predicates return true.
     * In the case that none are provided,
     * the predicate will always pass.
     *
     * @param exclusions the excluded predicates.
     * @param <T>        the tested type.
     * @return a new predicate requiring all the given predicates to fail.
     */
    @SafeVarargs
    public static <T> Predicate<T> noneOf(final Predicate<? super T>... exclusions) {
        return anyOf(exclusions).negate();
    }
}
