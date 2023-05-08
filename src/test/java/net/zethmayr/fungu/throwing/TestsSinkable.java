package net.zethmayr.fungu.throwing;

interface TestsSinkable {
    void sinking_givenSink_givenTarget_whenNoException_raiseDoesNotThrow();
    void sinking_givenSink_givenTarget_whenException_raiseThrows();
}
