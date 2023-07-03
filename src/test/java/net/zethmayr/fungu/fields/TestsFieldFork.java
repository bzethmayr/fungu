package net.zethmayr.fungu.fields;

import net.zethmayr.fungu.TestsTypedFork;
import org.hamcrest.Matcher;

import static net.zethmayr.fungu.test.MatcherFactory.has;
import static org.hamcrest.Matchers.allOf;

public interface TestsFieldFork extends TestsTypedFork {

    default <T, B> Matcher<FieldFork<T, B>> hasFieldValues(
            final Class<? extends HasX> topHas, final Class<T> topType, final T top,
            final Class<? extends HasX> bottomHas, final Class<B> bottomType, final B bottom
    ) {
        return allOf(
                has(FieldFork::topHas, topHas), has(FieldFork::topType, topType), hasTop(top),
                has(FieldFork::bottomHas, bottomHas), has(FieldFork::bottomType, bottomType), hasBottom(bottom)
        );
    }

}
