package net.zethmayr.fungu.fields;

import net.zethmayr.fungu.ReFork;
import net.zethmayr.fungu.core.ExceptionFactory;

import java.util.function.Supplier;

import static net.zethmayr.fungu.core.ExceptionFactory.becauseIllegal;
import static net.zethmayr.fungu.core.ExceptionFactory.becauseNotInstantiable;

/**
 * A fork with explicit fields and field types.
 * @param <T> the top type
 * @param <B> the bottom type
 */
public interface FieldFork<T, B> extends ReFork<T, B>, HasX {
    /**
     * Returns the gettable field interface associated with
     * the bottom item.
     * @return the top item field type
     */
    Class<? extends HasX> topHas();

    /**
     * Returns the gettable field interface associated with
     * the bottom item.
     * @return the bottom item field type.
     */
    Class<? extends HasX> bottomHas();

    /**
     * Explicitly indicates the top fork,
     * in cases where other types are equal.
     */
    final class Top implements HasX {
        private Top() {
            throw becauseNotInstantiable();
        }
    }

    /**
     * Explicitly indicates the bottom fork,
     * in cases where other types are equal.
     */
    final class Bottom implements HasX {
        private Bottom() {
            throw becauseNotInstantiable();
        }
    }

    /**
     * Provides transient (vs registered) getters for the top or bottom forks.
     * Both the field type and the value type must match for a getter to be returned.
     *
     * @param having     the having interface.
     * @param fieldClass the field type
     * @return a getter, or throws.
     * @param <H> the field interface.
     * @param <X> the value type.
     */
    @Override
    default <H extends HasX, X> Supplier<X> getGetter(
            final Class<H> having, final Class<X> fieldClass
    ) {
        if ((having == Top.class || having == topHas()) && fieldClass == topType()) {
            return () -> (X) top();
        }
        if ((having == Bottom.class || having == bottomHas()) && fieldClass == bottomType()) {
            return () -> (X) bottom();
        }
        throw becauseIllegal("This fork does not have field %s with type %s", having, fieldClass);
    }
}
