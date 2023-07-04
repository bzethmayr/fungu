package net.zethmayr.fungu;

import org.hamcrest.Matcher;

import static net.zethmayr.fungu.core.SuppressionConstants.SPECIFICATION;
import static net.zethmayr.fungu.test.MatcherFactory.has;
import static org.hamcrest.Matchers.allOf;

public interface TestsFork {

    default <T, B> Matcher<Fork<T, B>> hasTop(final T expectedTop) {
        return has(Fork::top, expectedTop);
    }

    default <T, B> Matcher<Fork<T, B>> hasBottom(final B expectedBottom) {
        return has(Fork::bottom, expectedBottom);
    }

    default <T, B> Matcher<Fork<T, B>> hasValues(final T expectedTop, final B expectedBottom) {
        return allOf(hasTop(expectedTop), hasBottom(expectedBottom));
    }

    default <T> Matcher<Fork<T, T>> hasValues(final T expectedBoth) {
        return hasValues(expectedBoth, expectedBoth);
    }

    @SuppressWarnings(SPECIFICATION)
    void equalValuedForksAreEqual();

    @SuppressWarnings(SPECIFICATION)
    void distinctValuedForksAreDistinct();

    @SuppressWarnings(SPECIFICATION)
    void with_givenNewValues_returnsSameConcreteType();

    void withBottom_givenNewBottomValue_returnsSameTopAndConcreteType();

    void withTop_givenNewTopValue_returnsSameBottomAndConcreteType();
}
