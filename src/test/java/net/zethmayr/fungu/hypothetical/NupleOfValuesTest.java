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
    public void nthMember_whenPopulatedAndExpectedType_givenFirstIndex_returnsFirstValue() {
        underTest = nupleOfValues(EXPECTED, UNEXPECTED);

        assertEquals(EXPECTED, firstString());
    }

    @Test
    @Override
    public void nthMember_whenPopulatedButNotExpectedType_givenFirstIndex_returnsFirstNull() {
        underTest = nupleOfValues(NOT_EVEN_WRONG, UNEXPECTED);

        assertNull(firstString());
    }

    @Test
    @Override
    public void nthMember_whenNoSuchIndex_throws() {
        underTest = nupleOfValues();

        assertThrows(IllegalArgumentException.class, () ->
                underTest.nthMember(0, String.class));
    }

    @Test
    @Override
    public void nthMember_whenPopulatedAndExpectedType_givenLastIndex_returnsLastValue() {
        underTest = nupleOfValues(UNEXPECTED, EXPECTED);

        assertEquals(EXPECTED, lastString());
    }

    @Test
    @Override
    public void nthMember_whenPopulatedButNotExpectedType_givenLastIndex_returnsLastNull() {
        underTest = nupleOfValues(UNEXPECTED, NOT_EVEN_WRONG);

        assertNull(lastString());
    }

    @Test
    @Override
    public void nthMember_whenAbsent_givenFirstIndex_returnsFirstNull() {
        underTest = nupleOfValues(NULL_STRING, UNEXPECTED);

        assertNull(firstString());
    }

    @Test
    @Override
    public void nthMember_whenAbsent_givenLastIndex_returnsLastNull() {
        underTest = nupleOfValues(UNEXPECTED, NULL_STRING);

        assertNull(lastString());
    }

    @Test
    @Override
    public void nthType_whenPopulatedAndExpected_givenFirstIndex_returnsFirstType() {
        underTest = nupleOfValues(EXPECTED, NOT_EVEN_WRONG);

        assertEquals(String.class, firstType());
    }

    @Test
    @Override
    public void nthType_whenPopulatedAndUnexpected_givenFirstIndex_returnsNull() {
        underTest = nupleOfValues(NOT_EVEN_WRONG, UNEXPECTED);

        assertNull(firstType());
    }

    @Test
    @Override
    public void nthType_whenNoSuchIndex_throws() {
        underTest = nupleOfValues(UNEXPECTED);

        assertThrows(IllegalArgumentException.class, () ->
                underTest.nthType(1, String.class));
    }

    @Test
    @Override
    public void nthType_whenAbsent_givenFirstIndex_returnsNull() {
        underTest = nupleOfValues(NULL_STRING, UNEXPECTED);

        assertNull(firstType());
    }

    @Test
    @Override
    public void nthType_whenAbsent_givenLastIndex_returnsNull() {
        underTest = nupleOfValues(UNEXPECTED, NULL_STRING);

        assertNull(underTest.nthType(1, String.class));
    }
}
