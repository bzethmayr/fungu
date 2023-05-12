package net.zethmayr.fungu.arc;

import java.io.Closeable;

public interface Testable extends Closeable {
    boolean isClosed();

    int getValue();
