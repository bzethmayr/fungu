package net.zethmayr.fungu.hypothetical;

import org.hamcrest.Matcher;

import static net.zethmayr.fungu.core.SuppressionConstants.SPECIFICATION;
import static net.zethmayr.fungu.test.MatcherFactory.has;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public interface TestsNuple {

    /**
     * Provides the test field with
     * the instance under test.
     *
     * @return the instance under test.
     */
    Nuple nupleUnderTest();

    default Matcher<Nuple> hasArityAtLeast(final int minimum) {
        return has(Nuple::arity, greaterThanOrEqualTo(minimum));
    }

    /**
     * @see #hasArityAtLeast(int)
     */
    @SuppressWarnings(SPECIFICATION)
    void arity_returnsSomeValue();

    default String firstString() {
        return nupleUnderTest().nthMember(0, String.class);
    }

    /**
     * @see #firstString()
     */
    @SuppressWarnings(SPECIFICATION)
    void nthMember_whenPopulatedAndExpectedType_givenFirstIndex_returnsFirstValue();

    /**
     * @see #firstString()
     */
    @SuppressWarnings(SPECIFICATION)
    void nthMember_whenPopulatedButNotExpectedType_givenFirstIndex_returnsFirstNull();

    @SuppressWarnings(SPECIFICATION)
    void nthMember_whenNoSuchIndex_throws();

    /**
     * Implementations with other than 2 members can override this.
     *
     * @return the last String, if any.
     */
    default String lastString() {
        return nupleUnderTest().nthMember(1, String.class);
    }

    default Class<String> firstType() {
        return nupleUnderTest().nthType(0, String.class);
    }

    /**
     * @see #lastString()
     */
    @SuppressWarnings(SPECIFICATION)
    void nthMember_whenPopulatedAndExpectedType_givenLastIndex_returnsLastValue();

    /**
     * @see #lastString()
     */
    @SuppressWarnings(SPECIFICATION)
    void nthMember_whenPopulatedButNotExpectedType_givenLastIndex_returnsLastNull();

    /**
     * @see #firstString()
     */
    @SuppressWarnings(SPECIFICATION)
    void nthMember_whenAbsent_givenFirstIndex_returnsFirstNull();

    /**
     * @see #lastString()
     */
    @SuppressWarnings(SPECIFICATION)
    void nthMember_whenAbsent_givenLastIndex_returnsLastNull();

    /**
     * @see #firstType()
     */
    @SuppressWarnings(SPECIFICATION)
    void nthType_whenPopulatedAndExpected_givenFirstIndex_returnsFirstType();

    /**
     * @see #firstType()
     */
    @SuppressWarnings(SPECIFICATION)
    void nthType_whenPopulatedAndUnexpected_givenFirstIndex_returnsNull();

    @SuppressWarnings(SPECIFICATION)
    void nthType_whenNoSuchIndex_throws();

    /**
     * This is a minimum requirement -
     * reifiable implementations can
     * validly return a known type.
     *
     * @see #firstType()
     */
    @SuppressWarnings(SPECIFICATION)
    void nthType_whenAbsent_givenFirstIndex_returnsNull();

    /**
     * This is a minimum requirement -
     * reifiable implementations can
     * validly return a known type.
     */
    @SuppressWarnings(SPECIFICATION)
    void nthType_whenAbsent_givenLastIndex_returnsNull();
}
