package net.zethmayr.fungu.arc;

import net.zethmayr.fungu.Testable;

import java.io.IOException;

public interface TestsCountedReference<T extends Testable, U extends CountedReference<T>> {

    U newReference();

    void countedReference_whenOpenedAndClosed_closesResourceOnce() throws IOException;

    void countedReference_whenOpenedTwiceAndClosedTwice_closesOneResourceOnce() throws IOException;
}
