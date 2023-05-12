package net.zethmayr.fungu;

import java.io.Closeable;

public interface Testable extends Closeable {
    boolean isClosed();

    int getValue();
}
