package net.zethmayr.fungu.hypothetical;

import org.junit.jupiter.api.Test;

public interface TestsNuple {

    /**
     * Provides the test field with
     * the instance under test.
     * @return the instance under test.
     */
    Nuple underTest();

    default String zerostString() {
        return underTest().nthMember(0, String.class);
    }

    /**
     * @see #zerostString()
     */
    @Test
    void nthMember_whenPopulatedAndExpectedType_givenZero_returnsFirstValue();

    /**
     * @see #zerostString()
     */
    @Test
    void nthMember_whenPopulatedButNotExpectedType_givenZero_returnsFirstNull();


    default String onendString() {
        return underTest().nthMember(1, String.class);
    }

    default Class<String> zerostType() {
        return underTest().nthType(0, String.class);
    }

    /**
     * @see #onendString()
     */
    @Test
    void nthMember_whenPopulatedAndExpectedType_givenOne_returnsSecondValue();

    /**
     * @see #onendString()
     */
    @Test
    void nthMember_whenPopulatedButNotExpectedType_givenOne_returnsSecondNull();

    /**
     * @see #zerostString()
     */
    @Test
    void nthMember_whenAbsent_givenZero_returnsFirstNull();

    /**
     * @see #onendString()
     */
    @Test
    void nthMember_whenAbsent_givenOne_returnsSecondNull();

    /**
     * @see #zerostType()
     */
    @Test
    void nthType_whenPopulatedAndExpected_givenZero_returnsFirstType();

    /**
     * @see #zerostType()
     */
    @Test
    void nthType_whenPopulatedAndUnexpected_givenZero_returnsNull();

    /**
     * This is a minimum requirement -
     * reifiable implementations can
     * validly return a known type.
     *
     * @see #zerostType()
     */
    @Test
    void nthType_whenAbsent_givenZero_returnsNull();

    /**
     * This is a minimum requirement -
     * reifiable implementations can
     * validly return a known type.
     */
    @Test
    void nthType_whenAbsent_givenOne_returnsNull();
}
