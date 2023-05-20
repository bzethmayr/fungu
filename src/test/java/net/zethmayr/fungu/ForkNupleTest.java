package net.zethmayr.fungu;

import net.zethmayr.fungu.hypothetical.Nuple;
import net.zethmayr.fungu.hypothetical.TestsNuple;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static net.zethmayr.fungu.ForkFactory.forkOf;
import static net.zethmayr.fungu.test.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

class ForkNupleTest implements TestsNuple {

    Nuple underTest;

    public Nuple underTest() {
        return underTest;
    }

    @Test
    @Override
    public void nthMember_whenPopulatedAndExpectedType_givenFirstIndex_returnsFirstValue() {
        underTest = forkOf(EXPECTED, UNEXPECTED);

        assertEquals(EXPECTED, firstString());
    }

    @Test
    @Override
    public void nthMember_whenPopulatedButNotExpectedType_givenFirstIndex_returnsFirstNull() {
        underTest = forkOf(NOT_EVEN_WRONG, UNEXPECTED);

        assertNull(firstString());
    }

    @Test
    @Override
    public void nthMember_whenNoSuchIndex_throws() {
        underTest = forkOf(UNEXPECTED, UNEXPECTED);

        assertThrows(IllegalArgumentException.class, () ->
                underTest.nthMember(3, String.class));
    }

    @Test
    @Override
    public void nthMember_whenPopulatedAndExpectedType_givenLastIndex_returnsLastValue() {
        underTest = forkOf(NOT_EVEN_WRONG, EXPECTED);

        assertEquals(EXPECTED, lastString());
    }

    @Test
    @Override
    public void nthMember_whenPopulatedButNotExpectedType_givenLastIndex_returnsLastNull() {
        underTest = forkOf(UNEXPECTED, NOT_EVEN_WRONG);

        assertNull(lastString());
    }

    @Test
    @Override
    public void nthMember_whenAbsent_givenFirstIndex_returnsFirstNull() {
        underTest = forkOf(null, UNEXPECTED);

        assertNull(firstString());
    }

    @Test
    @Override
    public void nthMember_whenAbsent_givenLastIndex_returnsLastNull() {
        underTest = forkOf(UNEXPECTED, null);

        assertNull(lastString());
    }

    @Test
    @Override
    public void nthType_whenPopulatedAndExpected_givenFirstIndex_returnsFirstType() {
        underTest = forkOf(EXPECTED, null);

        assertEquals(String.class, firstType());
    }

    @Test
    @Override
    public void nthType_whenPopulatedAndUnexpected_givenFirstIndex_returnsNull() {
        underTest = forkOf(NOT_EVEN_WRONG, UNEXPECTED);

        assertNull(firstType());
    }

    @Test
    @Override
    public void nthType_whenNoSuchIndex_throws() {
        underTest = forkOf(UNEXPECTED, UNEXPECTED);

        assertThrows(IllegalArgumentException.class, () ->
                underTest.nthType(4, String.class));
    }

    @Test
    @Override
    public void nthType_whenAbsent_givenFirstIndex_returnsNull() {
        underTest = forkOf(null, UNEXPECTED);

        assertNull(firstType());
    }

    @Test
    @Override
    public void nthType_whenAbsent_givenLastIndex_returnsNull() {
        underTest = forkOf(UNEXPECTED, null);

        assertNull(underTest.nthType(1, String.class));
    }
}
