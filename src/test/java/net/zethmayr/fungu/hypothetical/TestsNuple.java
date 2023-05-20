package net.zethmayr.fungu.hypothetical;

import org.junit.jupiter.api.Test;

public interface TestsNuple {

    /**
     * Provides the test field with
     * the instance under test.
     * @return the instance under test.
     */
    Nuple underTest();

    default String firstString() {
        return underTest().nthMember(0, String.class);
    }

    /**
     * @see #firstString()
     */
    @Test
    void nthMember_whenPopulatedAndExpectedType_givenFirstIndex_returnsFirstValue();

    /**
     * @see #firstString()
     */
    @Test
    void nthMember_whenPopulatedButNotExpectedType_givenFirstIndex_returnsFirstNull();
    
    @Test
    void nthMember_whenNoSuchIndex_throws();

    /**
     * Implementations with other than 2 members can override this.
     * @return the last String, if any.
     */
    default String lastString() {
        return underTest().nthMember(1, String.class);
    }

    default Class<String> firstType() {
        return underTest().nthType(0, String.class);
    }

    /**
     * @see #lastString()
     */
    @Test
    void nthMember_whenPopulatedAndExpectedType_givenLastIndex_returnsLastValue();

    /**
     * @see #lastString()
     */
    @Test
    void nthMember_whenPopulatedButNotExpectedType_givenLastIndex_returnsLastNull();

    /**
     * @see #firstString()
     */
    @Test
    void nthMember_whenAbsent_givenFirstIndex_returnsFirstNull();

    /**
     * @see #lastString()
     */
    @Test
    void nthMember_whenAbsent_givenLastIndex_returnsLastNull();

    /**
     * @see #firstType()
     */
    @Test
    void nthType_whenPopulatedAndExpected_givenFirstIndex_returnsFirstType();

    /**
     * @see #firstType()
     */
    @Test
    void nthType_whenPopulatedAndUnexpected_givenFirstIndex_returnsNull();
    
    @Test
    void nthType_whenNoSuchIndex_throws();

    /**
     * This is a minimum requirement -
     * reifiable implementations can
     * validly return a known type.
     *
     * @see #firstType()
     */
    @Test
    void nthType_whenAbsent_givenFirstIndex_returnsNull();

    /**
     * This is a minimum requirement -
     * reifiable implementations can
     * validly return a known type.
     */
    @Test
    void nthType_whenAbsent_givenLastIndex_returnsNull();
}
