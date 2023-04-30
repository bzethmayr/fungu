package net.zethmayr.fungu.hypothetical;

import org.junit.jupiter.api.Test;

import static net.zethmayr.fungu.hypothetical.Nuple.nupleOfTypedValues;
import static net.zethmayr.fungu.hypothetical.Nuple.nupleOfValues;
import static net.zethmayr.fungu.test.TestConstants.*;
import static net.zethmayr.fungu.test.TestHelper.invokeDefaultConstructor;
import static org.junit.jupiter.api.Assertions.*;

class NupleOfValuesTest implements TestsNuple {

    private Nuple underTest;

    @Test
    void nupleFactory_whenInstantiated_throwsInstead() {
        assertThrows(UnsupportedOperationException.class, () ->

                invokeDefaultConstructor(Nuple.NupleFactory.class));
    }

    @Test
    void nupleOfTypedValues_givenWrongNumberOfTypes_throws() {
        assertThrows(IllegalArgumentException.class, () ->

            nupleOfTypedValues(new Object[]{EXPECTED, UNEXPECTED}, String.class));
    }

    @Test
    void nupleOfTypedValues_givenWrongTypes_throws() {
        assertThrows(IllegalArgumentException.class, () ->

            nupleOfTypedValues(new Object[]{EXPECTED}, Integer.class));
    }

    @Test
    void nupleOfTypedValues_givenNoValues_createsEmptyNuple() {
        final Nuple underTest = nupleOfTypedValues(new Object[]{}, String.class, Integer.class);

        assertNull(underTest.nthRawMember(0));
        assertEquals(String.class, underTest.nthRawType(0));
        assertNull(underTest.nthRawMember(1));
        assertEquals(Integer.class, underTest.nthRawType(1));
    }

    @Override
    public Nuple underTest() {
        return underTest;
    }

    @Test
    @Override
    public void nthMember_whenPopulatedAndExpectedType_givenZero_returnsFirstValue() {
        underTest = nupleOfValues(EXPECTED, UNEXPECTED);

        assertEquals(EXPECTED, zerostString());
    }

    @Test
    @Override
    public void nthMember_whenPopulatedButNotExpectedType_givenZero_returnsFirstNull() {
        underTest = nupleOfValues(NOT_EVEN_WRONG, UNEXPECTED);

        assertNull(zerostString());
    }

    @Test
    @Override
    public void nthMember_whenPopulatedAndExpectedType_givenOne_returnsSecondValue() {
        underTest = nupleOfValues(UNEXPECTED, EXPECTED);

        assertEquals(EXPECTED, onendString());
    }

    @Test
    @Override
    public void nthMember_whenPopulatedButNotExpectedType_givenOne_returnsSecondNull() {
        underTest = nupleOfValues(UNEXPECTED, NOT_EVEN_WRONG);

        assertNull(onendString());
    }

    @Test
    @Override
    public void nthMember_whenAbsent_givenZero_returnsFirstNull() {
        underTest = nupleOfValues(NULL_STRING, UNEXPECTED);

        assertNull(zerostString());
    }

    @Test
    @Override
    public void nthMember_whenAbsent_givenOne_returnsSecondNull() {
        underTest = nupleOfValues(UNEXPECTED, NULL_STRING);

        assertNull(onendString());
    }

    @Test
    @Override
    public void nthType_whenPopulatedAndExpected_givenZero_returnsFirstType() {
        underTest = nupleOfValues(EXPECTED, NOT_EVEN_WRONG);

        assertEquals(String.class, zerostType());
    }

    @Test
    @Override
    public void nthType_whenPopulatedAndUnexpected_givenZero_returnsNull() {
        underTest = nupleOfValues(NOT_EVEN_WRONG, UNEXPECTED);

        assertNull(zerostType());
    }

    @Test
    @Override
    public void nthType_whenAbsent_givenZero_returnsNull() {
        underTest = nupleOfValues(NULL_STRING, UNEXPECTED);

        assertNull(zerostType());
    }

    @Test
    @Override
    public void nthType_whenAbsent_givenOne_returnsNull() {
        underTest = nupleOfValues(UNEXPECTED, NULL_STRING);

        assertNull(underTest.nthType(1, String.class));
    }
}
