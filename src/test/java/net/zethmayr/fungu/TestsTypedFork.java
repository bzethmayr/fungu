package net.zethmayr.fungu;

import static net.zethmayr.fungu.core.SuppressionConstants.SPECIFICATION;

public interface TestsTypedFork extends TestsFork {

    @SuppressWarnings(SPECIFICATION)
    void sameTypedEmptyForksAreEqual();

    @SuppressWarnings(SPECIFICATION)
    void distinctTypedEmptyForksAreDistinct();
}
