package net.zethmayr.fungu;

import net.zethmayr.fungu.hypothetical.Nuple;
import net.zethmayr.fungu.hypothetical.TestsNuple;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static net.zethmayr.fungu.ForkFactory.forkOf;
import static net.zethmayr.fungu.test.TestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ForkNupleTest implements TestsNuple {

    Nuple underTest;

    public Nuple underTest() {
        return underTest;
    }

    @Test
    @Override
    public void nthMember_whenPopulatedAndExpectedType_givenZero_returnsFirstValue() {
        underTest = forkOf(EXPECTED, UNEXPECTED);

        Assertions.assertEquals(EXPECTED, zerostString());
    }

    @Test
    @Override
    public void nthMember_whenPopulatedButNotExpectedType_givenZero_returnsFirstNull() {
        underTest = forkOf(NOT_EVEN_WRONG, UNEXPECTED);

        assertNull(zerostString());
    }

    @Test
    @Override
    public void nthMember_whenPopulatedAndExpectedType_givenOne_returnsSecondValue() {
        underTest = forkOf(NOT_EVEN_WRONG, EXPECTED);

        Assertions.assertEquals(EXPECTED, onendString());
    }

    @Test
    @Override
    public void nthMember_whenPopulatedButNotExpectedType_givenOne_returnsSecondNull() {
        underTest = forkOf(UNEXPECTED, NOT_EVEN_WRONG);

        assertNull(onendString());
    }

    @Test
    @Override
    public void nthMember_whenAbsent_givenZero_returnsFirstNull() {
        underTest = forkOf(null, UNEXPECTED);

        assertNull(zerostString());
    }

    @Test
    @Override
    public void nthMember_whenAbsent_givenOne_returnsSecondNull() {
        underTest = forkOf(UNEXPECTED, null);

        assertNull(onendString());
    }

    @Test
    @Override
    public void nthType_whenPopulatedAndExpected_givenZero_returnsFirstType() {
        underTest = forkOf(EXPECTED, null);

        assertEquals(String.class, zerostType());
    }

    @Test
    @Override
    public void nthType_whenPopulatedAndUnexpected_givenZero_returnsNull() {
        underTest = forkOf(NOT_EVEN_WRONG, UNEXPECTED);

        assertNull(zerostType());
    }

    @Test
    @Override
    public void nthType_whenAbsent_givenZero_returnsNull() {
        underTest = forkOf(null, UNEXPECTED);

        assertNull(zerostType());
    }

    @Test
    @Override
    public void nthType_whenAbsent_givenOne_returnsNull() {
        underTest = forkOf(UNEXPECTED, null);

        assertNull(underTest.nthType(1, String.class));
    }
}
