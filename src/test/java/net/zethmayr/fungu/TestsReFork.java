package net.zethmayr.fungu;

public interface TestsReFork extends TestsFork {

    void sameTypedEmptyForksAreEqual();

    void distinctTypedEmptyForksAreDistinct();
}
