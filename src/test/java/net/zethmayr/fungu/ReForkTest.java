package net.zethmayr.fungu;

import net.zethmayr.fungu.hypothetical.Nuple;
import net.zethmayr.fungu.hypothetical.TestsNuple;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static net.zethmayr.fungu.ForkFactory.reForkOf;
import static net.zethmayr.fungu.test.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ReForkTest implements TestsNuple {

    private ReFork<?, ?> underTest;

    @Test
    void reForkOf_givenTypesAndNulls_returnsFork_withTypedNulls() {

        final ReFork<String, Integer> underTest = reForkOf(String.class, null, Integer.class, null);

        assertEquals(String.class, underTest.topType());
        assertNull(underTest.top());
        assertEquals(Integer.class, underTest.bottomType());
        assertNull(underTest.bottom());
    }

    @Test
    void reForkOf_givenTypesAndValues_returnsFork_withTypedValues() {

        final ReFork<String, Integer> underTest = reForkOf(String.class, EXPECTED, Integer.class, 0);

        assertEquals(String.class, underTest.topType());
        assertEquals(EXPECTED, underTest.top());
        assertEquals(Integer.class, underTest.bottomType());
        assertEquals(0, underTest.bottom());
    }

    @Test
    void reForkOf_givenReifiableValues_returnsFork_withCorrectlyInferredTypedValues() {

        final ReFork<String, Integer> underTest = reForkOf(EXPECTED, 0);

        assertEquals(String.class, underTest.topType());
        assertEquals(EXPECTED, underTest.top());
        assertEquals(Integer.class, underTest.bottomType());
        assertEquals(0, underTest.bottom());
    }

    @Test
    void reForkOf_givenSubclassedValues_returnsFork_withOverlyStrictTypedValues() {

        final ReFork<List<Integer>, Integer> underTest = reForkOf(new ArrayList<>(), 0);

        assertEquals(ArrayList.class, underTest.topType()); // vs List
        assertEquals(emptyList(), underTest.top());
        assertEquals(Integer.class, underTest.bottomType());
        assertEquals(0, underTest.bottom());
    }

    @Test
    void with_givenNewValues_returnsFork_withSameTypesAndNewValues() {
        final ReFork<String, Integer> source = reForkOf(EXPECTED, 3);

        final ReFork<String, Integer> underTest = (ReFork<String, Integer>) source.with(null, null);
        assertEquals(String.class, underTest.topType());
        assertNull(underTest.top());
        assertEquals(Integer.class, underTest.bottomType());
        assertNull(underTest.bottom());
    }

    @Override
    public Nuple underTest() {
        return underTest;
    }

    @Test
    @Override
    public void nthMember_whenPopulatedAndExpectedType_givenZero_returnsFirstValue() {
        underTest = reForkOf(EXPECTED, UNEXPECTED);

        assertEquals(EXPECTED, zerostString());
    }

    @Test
    @Override
    public void nthMember_whenPopulatedButNotExpectedType_givenZero_returnsFirstNull() {
        underTest = reForkOf(NOT_EVEN_WRONG, UNEXPECTED);

        assertNull(zerostString());
    }

    @Test
    @Override
    public void nthMember_whenPopulatedAndExpectedType_givenOne_returnsSecondValue() {
        underTest = reForkOf(UNEXPECTED, EXPECTED);

        assertEquals(EXPECTED, onendString());
    }

    @Test
    @Override
    public void nthMember_whenPopulatedButNotExpectedType_givenOne_returnsSecondNull() {
        underTest = reForkOf(UNEXPECTED, NOT_EVEN_WRONG);

        assertNull(onendString());
    }

    @Test
    @Override
    public void nthMember_whenAbsent_givenZero_returnsFirstNull() {
        underTest = reForkOf(String.class, NULL_STRING, String.class, UNEXPECTED);

        assertNull(zerostString());
    }


   @Test
   @Override
    public void nthMember_whenAbsent_givenOne_returnsSecondNull() {
        underTest = reForkOf(String.class, UNEXPECTED, String.class, NULL_STRING);

        assertNull(onendString());
    }

    @Test
    @Override
    public void nthType_whenPopulatedAndExpected_givenZero_returnsFirstType() {
        underTest = reForkOf(EXPECTED, NOT_EVEN_WRONG);

        assertEquals(String.class, zerostType());
    }

    @Test
    @Override
    public void nthType_whenPopulatedAndUnexpected_givenZero_returnsNull() {
        underTest = reForkOf(NOT_EVEN_WRONG, UNEXPECTED);

        assertNull(zerostType());
    }

    @Test
    @Override
    public void nthType_whenAbsent_givenZero_returnsNull() {
        underTest = reForkOf(String.class, NULL_STRING, Object.class, UNEXPECTED);

        assertEquals(String.class, zerostType());
    }

    @Test
    @Override
    public void nthType_whenAbsent_givenOne_returnsNull() {
        underTest = reForkOf(Object.class, UNEXPECTED, String.class, NULL_STRING);

        assertEquals(String.class, underTest.nthType(1, String.class));
    }
}
