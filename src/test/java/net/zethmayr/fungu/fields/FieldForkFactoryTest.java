package net.zethmayr.fungu.fields;

import net.zethmayr.fungu.test.TestConstants;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

import static net.zethmayr.fungu.fields.FieldForkFactory.fieldForkOf;
import static net.zethmayr.fungu.test.MatcherFactory.has;
import static net.zethmayr.fungu.test.TestConstants.*;
import static net.zethmayr.fungu.test.TestHelper.invokeDefaultConstructor;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.Matchers.is;
class FieldForkFactoryTest {

    @Test
    void fieldForkFactory_whenInstantiated_throwsInstead() {
        assertThrows(UnsupportedOperationException.class, () ->

                invokeDefaultConstructor(FieldForkFactory.class));
    }

    private <T, B> Matcher<FieldFork<T, B>> hasFieldValues(
            final Class<? extends HasX> topHas, final Class<T> topType, final T top,
            final Class<? extends HasX> bottomHas, final Class<B> bottomType, final B bottom
    ) {
        return allOf(
                has(FieldFork::topHas, topHas), has(FieldFork::topType, topType), has(FieldFork::top, top),
                has(FieldFork::bottomHas, bottomHas), has(FieldFork::bottomType, bottomType), has(FieldFork::bottom, bottom)
        );
    }

    @Test
    void fieldForkOf_givenFieldsAndTypesAndValues_returnsForkWithExpectedValues() {

        final FieldFork<String, String> underTest = fieldForkOf(
                HasFoo.class, String.class, EXPECTED,
                HasBar.class, String.class, SHIBBOLETH
        );

        assertThat(underTest, hasFieldValues(
                HasFoo.class, String.class, EXPECTED,
                HasBar.class, String.class, SHIBBOLETH
        ));
    }

    @Test
    void with_whenFieldsAndTypesAndValues_returnsForkWithExpectedNewValues() {
        final FieldFork<String, String> underTest = fieldForkOf(
                HasBar.class, String.class, UNEXPECTED,
                HasFoo.class, String.class, NULL_STRING
        );

        final FieldFork<String, String> result = (FieldFork<String, String>) underTest.with(EXPECTED, SHIBBOLETH);

        assertThat(result, hasFieldValues(
                HasBar.class, String.class, EXPECTED,
                HasFoo.class, String.class, SHIBBOLETH
        ));
    }
}