package net.zethmayr.fungu.throwing;

import static net.zethmayr.fungu.core.SuppressionConstants.SPECIFICATION;

public interface TestsRevenant {

    @SuppressWarnings(SPECIFICATION)
    void raise_givenExpected_throwsExpected();

    @SuppressWarnings(SPECIFICATION)
    void raise_givenNone_throwsNone();

    @SuppressWarnings(SPECIFICATION)
    void raiseChecked_givenExpected_throwsExpected();

    @SuppressWarnings(SPECIFICATION)
    void raiseChecked_givenUnexpected_throwsWrapped();

    @SuppressWarnings(SPECIFICATION)
    void raiseChecked_givenNone_throwsNone();

    @SuppressWarnings(SPECIFICATION)
    void raiseOr_givenExpected_throwsExpected();

    @SuppressWarnings(SPECIFICATION)
    void raiseOr_givenUnexpected_returnsChaining_raiseChecked_givenUnexpected_throwsWrapped();

    @SuppressWarnings(SPECIFICATION)
    void raiseOr_givenNone_returnsSelf();
}
