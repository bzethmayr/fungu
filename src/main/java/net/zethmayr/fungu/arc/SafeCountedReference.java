package net.zethmayr.fungu.arc;

import java.io.Closeable;

import static net.zethmayr.fungu.CloseableFactory.closeIntercepted;
import static net.zethmayr.fungu.ConsumerFactory.nothing;

public abstract class SafeCountedReference<T extends Closeable> extends CountedReference<T> {

    private final T closeProtected;

    protected SafeCountedReference(final Class<T> resourceInterface, final Class<?>... additionalInterfaces) {
        super();
        closeProtected = closeIntercepted(resourceInterface, super.getResource(), nothing(), additionalInterfaces);
    }

    @Override
    public T getResource() {
        return closeProtected;
    }
}
