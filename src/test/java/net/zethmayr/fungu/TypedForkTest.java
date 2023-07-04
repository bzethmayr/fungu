package net.zethmayr.fungu;

import net.zethmayr.fungu.hypothetical.Nuple;
import net.zethmayr.fungu.hypothetical.TestsNuple;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static net.zethmayr.fungu.ForkFactory.reForkOf;
import static net.zethmayr.fungu.test.TestConstants.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class TypedForkTest implements TestsTypedFork, TestsNuple {

    private TypedFork<?, ?> underTest;

    @Test
    @Override
    public void arity_returnsSomeValue() {
        underTest = reForkOf(0, 0);

        assertThat(underTest, hasArityAtLeast(2));
    }

    @Test
    void reForkOf_givenTypesAndNulls_returnsFork_withTypedNulls() {

        final TypedFork<String, Integer> underTest = reForkOf(String.class, null, Integer.class, null);

        assertEquals(String.class, underTest.topType());
        assertNull(underTest.top());
        assertEquals(Integer.class, underTest.bottomType());
        assertNull(underTest.bottom());
    }

    @Test
    void reForkOf_givenTypesAndValues_returnsFork_withTypedValues() {

        final TypedFork<String, Integer> underTest = reForkOf(String.class, EXPECTED, Integer.class, 0);

        assertEquals(String.class, underTest.topType());
        assertEquals(EXPECTED, underTest.top());
        assertEquals(Integer.class, underTest.bottomType());
        assertEquals(0, underTest.bottom());
    }

    @Test
    void reForkOf_givenReifiableValues_returnsFork_withCorrectlyInferredTypedValues() {

        final TypedFork<String, Integer> underTest = reForkOf(EXPECTED, 0);

        assertEquals(String.class, underTest.topType());
        assertEquals(EXPECTED, underTest.top());
        assertEquals(Integer.class, underTest.bottomType());
        assertEquals(0, underTest.bottom());
    }

    @Test
    void reForkOf_givenSubclassedValues_returnsFork_withOverlyStrictTypedValues() {

        final TypedFork<List<Integer>, Integer> underTest = reForkOf(new ArrayList<>(), 0);

        assertEquals(ArrayList.class, underTest.topType()); // vs List
        assertEquals(emptyList(), underTest.top());
        assertEquals(Integer.class, underTest.bottomType());
        assertEquals(0, underTest.bottom());
    }

    @Test
    void with_givenNewValues_returnsFork_withSameTypesAndNewValues() {
        final TypedFork<String, Integer> source = reForkOf(EXPECTED, 3);

        final TypedFork<String, Integer> underTest = (TypedFork<String, Integer>) source.with(null, null);
        assertEquals(String.class, underTest.topType());
        assertNull(underTest.top());
        assertEquals(Integer.class, underTest.bottomType());
        assertNull(underTest.bottom());
    }

    @Test
    @Override
    public void sameTypedEmptyForksAreEqual() {
        underTest = reForkOf(String.class, null, String.class, null);
        final TypedFork<String, String> equivalent = reForkOf(String.class, null, String.class, null);

        assertThat(underTest, not(sameInstance(equivalent)));
        assertEquals(equivalent, underTest);
        assertEquals(underTest, equivalent);
    }

    @Test
    @Override
    public void distinctTypedEmptyForksAreDistinct() {
        underTest = reForkOf(String.class, null, String.class, null);
        final TypedFork<String, Integer> distinct = reForkOf(String.class, null, Integer.class, null);

        assertThat(underTest, not(sameInstance(distinct)));
        assertNotEquals(distinct, underTest);
        assertNotEquals(underTest, distinct);
    }

    @Test
    @Override
    public void equalValuedForksAreEqual() {
        underTest = reForkOf(String.class, EXPECTED, String.class, EXPECTED);
        final TypedFork<String, String> equivalent = reForkOf(String.class, EXPECTED, String.class, EXPECTED);

        assertThat(underTest, not(sameInstance(equivalent)));
        assertEquals(equivalent, underTest);
        assertEquals(underTest, equivalent);
    }

    @Test
    @Override
    public void distinctValuedForksAreDistinct() {
        underTest = reForkOf(EXPECTED, EXPECTED);
        final TypedFork<String, String> distinct = reForkOf(UNEXPECTED, UNEXPECTED);

        assertThat(underTest, not(sameInstance(distinct)));
        assertNotEquals(distinct, underTest);
        assertNotEquals(underTest, distinct);
    }

    @Test
    @Override
    public void with_givenNewValues_returnsSameConcreteType() {
        final String originalValue = UNEXPECTED;
        final Fork<String, String> underTest = reForkOf(originalValue, originalValue);
        final Class<?> expectedClass = underTest.getClass();

        final Fork<String, String> revalued = underTest.with(EXPECTED, SHIBBOLETH);

        assertThat(revalued, instanceOf(expectedClass));
        assertThat(underTest, hasValues(originalValue));
        assertThat(revalued, hasValues(EXPECTED, SHIBBOLETH));
    }

    @Test
    @Override
    public void withBottom_givenNewBottomValue_returnsSameTopAndConcreteType() {
        final String originalValue = SHIBBOLETH;
        final Fork<String, String> underTest = reForkOf(originalValue, originalValue);

        final Fork<String, String> revalued = underTest.withBottom(EXPECTED);

        assertThat(revalued, instanceOf(underTest.getClass()));
        assertThat(revalued, not(sameInstance(underTest)));
        assertThat(revalued, hasValues(SHIBBOLETH, EXPECTED));
        assertThat(underTest, hasValues(originalValue));
    }

    @Test
    @Override
    public void withTop_givenNewTopValue_returnsSameBottomAndConcreteType() {
        final String originalValue = SHIBBOLETH;
        final Fork<String, String> underTest = reForkOf(originalValue, originalValue);

        final Fork<String, String> revalued = underTest.withTop(EXPECTED);

        assertThat(revalued, instanceOf(underTest.getClass()));
        assertThat(revalued, not(sameInstance(underTest)));
        assertThat(revalued, hasValues(EXPECTED, SHIBBOLETH));
        assertThat(underTest, hasValues(originalValue));
    }

    @Override
    public Nuple nupleUnderTest() {
        return underTest;
    }

    @Test
    @Override
    public void nthMember_whenPopulatedAndExpectedType_givenFirstIndex_returnsFirstValue() {
        underTest = reForkOf(EXPECTED, UNEXPECTED);

        assertEquals(EXPECTED, firstString());
    }

    @Test
    @Override
    public void nthMember_whenPopulatedButNotExpectedType_givenFirstIndex_returnsFirstNull() {
        underTest = reForkOf(NOT_EVEN_WRONG, UNEXPECTED);

        assertNull(firstString());
    }

    @Test
    @Override
    public void nthMember_whenNoSuchIndex_throws() {
        underTest = reForkOf(UNEXPECTED, EXPECTED);

        assertThrows(IllegalArgumentException.class, () ->
                underTest.nthMember(5, String.class));
    }

    @Test
    @Override
    public void nthMember_whenPopulatedAndExpectedType_givenLastIndex_returnsLastValue() {
        underTest = reForkOf(UNEXPECTED, EXPECTED);

        assertEquals(EXPECTED, lastString());
    }

    @Test
    @Override
    public void nthMember_whenPopulatedButNotExpectedType_givenLastIndex_returnsLastNull() {
        underTest = reForkOf(UNEXPECTED, NOT_EVEN_WRONG);

        assertNull(lastString());
    }

    @Test
    @Override
    public void nthMember_whenAbsent_givenFirstIndex_returnsFirstNull() {
        underTest = reForkOf(String.class, NULL_STRING, String.class, UNEXPECTED);

        assertNull(firstString());
    }


    @Test
    @Override
    public void nthMember_whenAbsent_givenLastIndex_returnsLastNull() {
        underTest = reForkOf(String.class, UNEXPECTED, String.class, NULL_STRING);

        assertNull(lastString());
    }

    @Test
    @Override
    public void nthType_whenPopulatedAndExpected_givenFirstIndex_returnsFirstType() {
        underTest = reForkOf(EXPECTED, NOT_EVEN_WRONG);

        assertEquals(String.class, firstType());
    }

    @Test
    @Override
    public void nthType_whenPopulatedAndUnexpected_givenFirstIndex_returnsNull() {
        underTest = reForkOf(NOT_EVEN_WRONG, UNEXPECTED);

        assertNull(firstType());
    }

    @Test
    @Override
    public void nthType_whenNoSuchIndex_throws() {
        underTest = reForkOf(EXPECTED, UNEXPECTED);

        assertThrows(IllegalArgumentException.class, () ->
                underTest.nthType(-1, String.class));
    }

    @Test
    @Override
    public void nthType_whenAbsent_givenFirstIndex_returnsNull() {
        underTest = reForkOf(String.class, NULL_STRING, Object.class, UNEXPECTED);

        assertEquals(String.class, firstType());
    }

    @Test
    @Override
    public void nthType_whenAbsent_givenLastIndex_returnsNull() {
        underTest = reForkOf(Object.class, UNEXPECTED, String.class, NULL_STRING);

        assertEquals(String.class, underTest.nthType(1, String.class));
    }
}
