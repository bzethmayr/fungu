package net.zethmayr.fungu.fields;

import net.zethmayr.fungu.TestsReFork;
import org.hamcrest.Matcher;

import static net.zethmayr.fungu.test.MatcherFactory.has;
import static org.hamcrest.Matchers.allOf;

public interface TestsFieldFork extends TestsReFork {

    default <T, B> Matcher<FieldFork<T, B>> hasFieldValues(
            final Class<? extends HasX> topHas, final Class<T> topType, final T top,
            final Class<? extends HasX> bottomHas, final Class<B> bottomType, final B bottom
    ) {
        return allOf(
                has(FieldFork::topHas, topHas), has(FieldFork::topType, topType), has(FieldFork::top, top),
                has(FieldFork::bottomHas, bottomHas), has(FieldFork::bottomType, bottomType), has(FieldFork::bottom, bottom)
        );
    }

}
