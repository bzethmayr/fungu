package net.zethmayr.fungu.fields;

import net.zethmayr.fungu.ReFork;
import net.zethmayr.fungu.core.ExceptionFactory;

import java.util.function.Supplier;

public interface FieldFork<T, B> extends ReFork<T, B>, HasX {
    Class<? extends HasX> topHas();
    Class<? extends HasX> bottomHas();

    @Override
    default <H extends HasX, X> Supplier<X> getGetter(
            final Class<H> having, final Class<X> fieldClass
    ) {
        if (having == topHas() && fieldClass == topType()) {
            return () -> (X) top();
        }
        if (having == bottomHas() && fieldClass == bottomType()) {
            return () -> (X) bottom();
        }
        throw ExceptionFactory.becauseIllegal("This fork does not have field %s with type %s", having, fieldClass);
    }
}
