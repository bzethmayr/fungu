package net.zethmayr.fungu;

import net.zethmayr.fungu.core.ExceptionFactory;
import net.zethmayr.fungu.core.SuppressionConstants;

import java.util.function.Predicate;

/**
 * Primitive and composed predicates
 */
public final class PredicateFactory {

    public static final Predicate<Object> NEVER = x -> false;
    public static final Predicate<Object> ALWAYS = x -> true;

    private PredicateFactory() {
        throw ExceptionFactory.becauseStaticsOnly();
    }

    @SuppressWarnings(SuppressionConstants.CHECK_ASSURED)
    private static <T> Predicate<T> genericDegenerate(final Predicate<Object> degenerate) {
        return (Predicate<T>) degenerate;
    }

    @SafeVarargs
    public static <T> Predicate<T> allOf(final Predicate<? super T>... inclusions) {
        Predicate<T> requirement = genericDegenerate(ALWAYS);
        for (Predicate<? super T> further : inclusions) {
            requirement = requirement.and(further);
        }
        return requirement;
    }

    @SafeVarargs
    public static <T> Predicate<T> anyOf(final Predicate<? super T>... inclusions) {
        Predicate<T> requirement = genericDegenerate(NEVER);
        for (Predicate<? super T> further : inclusions) {
            requirement = requirement.or(further);
        }
        return requirement;
    }

    @SafeVarargs
    public static <T> Predicate<T> noneOf(final Predicate<? super T>... exclusions) {
        return anyOf(exclusions).negate();
    }
}
