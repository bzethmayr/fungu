package net.zethmayr.fungu.fields;

import net.zethmayr.fungu.fields.FieldFork.Bottom;
import net.zethmayr.fungu.fields.FieldFork.Top;
import net.zethmayr.fungu.hypothetical.TestsNuple;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static net.zethmayr.fungu.core.ExceptionFactory.becauseUnsupported;
import static net.zethmayr.fungu.fields.FieldForkFactory.fieldForkOf;
import static net.zethmayr.fungu.test.MatcherFactory.has;
import static net.zethmayr.fungu.test.TestConstants.*;
import static net.zethmayr.fungu.test.TestHelper.invokeDefaultConstructor;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class FieldForkTest implements TestsFieldFork, TestsNuple {

    private FieldFork<?, ?> underTest;

    public FieldFork<?, ?> underTest() {
        return underTest;
    }

    private static FieldFork<String, String> topExpected() {
        return fieldForkOf(
                HasFoo.class, EXPECTED, HasBar.class, UNEXPECTED);
    }

    private static FieldFork<String, String> bottomExpected() {
        return fieldForkOf(
                HasFoo.class, UNEXPECTED, HasBar.class, EXPECTED);
    }

    private static Matcher<Supplier<String>> hasExpected() {
        return has(Supplier::get, EXPECTED);
    }

    @Test
    void fieldForkFactory_whenInstantiated_throwsInstead() {
        assertThrows(UnsupportedOperationException.class, () ->

                invokeDefaultConstructor(FieldForkFactory.class));
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
    void getGetter_givenTopType_returnsTopGetter() {
        final FieldFork<String, String> underTest = topExpected();

        final Supplier<String> boundGetter = underTest.getGetter(HasFoo.class, String.class);

        assertThat(boundGetter, hasExpected());
    }

    @Test
    void getGetter_givenTopSymbol_returnsTopGetter() {
        final FieldFork<String, String> underTest = topExpected();

        final Supplier<String> boundGetter = underTest.getGetter(Top.class, String.class);

        assertThat(boundGetter, hasExpected());
    }

    @Test
    void getGetter_givenTopTypeOnly_returnsTopGetter() {
        final FieldFork<String, String> underTest = topExpected();

        final Supplier<String> boundGetter = underTest.getGetter(HasFoo.class, null);

        assertThat(boundGetter, hasExpected());
    }

    @Test
    void getGetter_givenTopSymbolOnly_returnsTopGetter() {
        final FieldFork<String, String> underTest = topExpected();

        final Supplier<String> boundGetter = underTest.getGetter(Top.class, null);

        assertThat(boundGetter, hasExpected());
    }

    @Test
    void getGetter_givenBottomType_returnsBottomGetter() {
        final FieldFork<String, String> underTest = bottomExpected();

        final Supplier<String> boundGetter = underTest.getGetter(HasBar.class, String.class);

        assertThat(boundGetter, hasExpected());
    }

    @Test
    void getGetter_givenBottomSymbol_returnsBottomGetter() {
        final FieldFork<String, String> underTest = bottomExpected();

        final Supplier<String> boundGetter = underTest.getGetter(Bottom.class, String.class);

        assertThat(boundGetter, hasExpected());
    }

    @Test
    void getGetter_givenBottomTypeOnly_returnsBottomGetter() {
        final FieldFork<String, String> underTest = bottomExpected();

        final Supplier<String> boundGetter = underTest.getGetter(HasBar.class, null);

        assertThat(boundGetter, hasExpected());
    }

    @Test
    void getGetter_givenUnsupportedInterface_throws() {
        final FieldFork<String, String> underTest = topExpected();
        assertThrows(IllegalArgumentException.class, () ->

                underTest.getGetter(HasX.class, String.class));
    }

    @Test
    void getGetter_givenUnsupportedInterfaceOnly_throws() {
        final FieldFork<String, String> underTest = topExpected();
        assertThrows(IllegalArgumentException.class, () ->

                underTest.getGetter(HasX.class, null));
    }

    @Test
    void top_whenInstantiated_throwsInstead() {
        assertThrows(UnsupportedOperationException.class, () ->

                invokeDefaultConstructor(Top.class));
    }

    @Test
    void bottom_whenInstantiated_throwsInstead() {
        assertThrows(UnsupportedOperationException.class, () ->

                invokeDefaultConstructor(Bottom.class));
    }

    private static FieldFork<String, String> empty(
          final Class<? extends HasX> top, final Class<? extends HasX> bottom
    ) {
        return fieldForkOf(top, String.class, null, bottom, String.class, null);
    }

    @Test
    @Override
    public void sameTypedEmptyForksAreEqual() {
        final FieldFork<String, String> underTest = empty(HasBar.class, HasFoo.class);
        final FieldFork<String, String> equivalent = empty(HasBar.class, HasFoo.class);

        assertThat(underTest, not(sameInstance(equivalent)));
        assertEquals(equivalent, underTest);
        assertEquals(underTest, equivalent);
    }

    @Test
    @Override
    public void distinctTypedEmptyForksAreDistinct() {
        final FieldFork<String, String> underTest = empty(HasFoo.class, HasBar.class);
        final FieldFork<String, String> distinct = empty(HasBar.class, HasFoo.class);

        assertThat(underTest, not(sameInstance(distinct)));
        assertNotEquals(distinct, underTest);
        assertNotEquals(underTest, distinct);
    }

    @Test
    @Override
    public void equalValuedForksAreEqual() {
        final FieldFork<String, String> underTest = topExpected();
        final FieldFork<String, String> equivalent = topExpected();

        assertThat(underTest, not(sameInstance(equivalent)));
        assertEquals(equivalent, underTest);
        assertEquals(underTest, equivalent);
    }

    @Test
    @Override
    public void distinctValuedForksAreDistinct() {
        final FieldFork<String, String> underTest = topExpected();
        final FieldFork<String, String> distinct = bottomExpected();

        assertThat(underTest, not(sameInstance(distinct)));
        assertNotEquals(distinct, underTest);
        assertNotEquals(underTest, distinct);
    }

    @Test
    @Override
    public void nthMember_whenPopulatedAndExpectedType_givenFirstIndex_returnsFirstValue() {
        underTest = fieldForkOf(HasFoo.class, EXPECTED, HasBar.class, SHIBBOLETH);

        assertEquals(EXPECTED, firstString());
    }

    @Test
    @Override
    public void nthMember_whenPopulatedButNotExpectedType_givenFirstIndex_returnsFirstNull() {
        underTest = fieldForkOf(HasX.class, NOT_EVEN_WRONG, HasFoo.class, UNEXPECTED);

        assertNull(firstString());
    }

    @Test
    @Override
    public void nthMember_whenNoSuchIndex_throws() {
        underTest = topExpected();

        assertThrows(IllegalArgumentException.class, () ->
                underTest.nthMember(4, String.class));
    }

    @Test
    @Override
    public void nthMember_whenPopulatedAndExpectedType_givenLastIndex_returnsLastValue() {
        underTest = bottomExpected();

        assertEquals(EXPECTED, lastString());
    }

    @Test
    @Override
    public void nthMember_whenPopulatedButNotExpectedType_givenLastIndex_returnsLastNull() {
        underTest = fieldForkOf(HasBar.class, UNEXPECTED, HasX.class, NOT_EVEN_WRONG);

        assertNull(lastString());
    }

    @Test
    @Override
    public void nthMember_whenAbsent_givenFirstIndex_returnsFirstNull() {
        underTest = fieldForkOf(HasFoo.class, String.class, null, HasBar.class, String.class, UNEXPECTED);

        assertNull(firstString());
    }

    @Test
    @Override
    public void nthMember_whenAbsent_givenLastIndex_returnsLastNull() {
        underTest = fieldForkOf(HasFoo.class, String.class, UNEXPECTED, HasBar.class, String.class, null);

        assertNull(lastString());
    }

    @Test
    @Override
    public void nthType_whenPopulatedAndExpected_givenFirstIndex_returnsFirstType() {
        underTest = topExpected();

        assertEquals(String.class, firstType());
    }

    @Test
    @Override
    public void nthType_whenPopulatedAndUnexpected_givenFirstIndex_returnsNull() {
        underTest = fieldForkOf(HasX.class, NOT_EVEN_WRONG, HasBar.class, UNEXPECTED);

        assertNull(firstType());
    }

    @Test
    @Override
    public void nthType_whenNoSuchIndex_throws() {
        underTest = topExpected();

        assertThrows(IllegalArgumentException.class, () ->
                underTest.nthType(5, String.class));
    }

    @Override
    public void nthType_whenAbsent_givenFirstIndex_returnsNull() {
        throw becauseUnsupported("The nth type is never absent in this implementation");
    }

    @Override
    public void nthType_whenAbsent_givenLastIndex_returnsNull() {
        throw becauseUnsupported("The nth type is never absent in this implementation");
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
