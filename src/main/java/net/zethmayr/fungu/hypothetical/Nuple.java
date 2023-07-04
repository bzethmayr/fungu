package net.zethmayr.fungu.hypothetical;

import net.zethmayr.fungu.core.ExceptionFactory;
import net.zethmayr.fungu.core.SuppressionConstants;
import net.zethmayr.fungu.hypothetical.Nuple.NupleFactory.ArrayNuple;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.System.arraycopy;
import static java.util.Objects.nonNull;
import static net.zethmayr.fungu.core.ExceptionFactory.becauseFactory;
import static net.zethmayr.fungu.core.ExceptionFactory.becauseIllegal;
import static net.zethmayr.fungu.core.TypeHelper.defaultingType;

/**
 * Superinterface of strongly-typed classes which also have finite, arbitrary n-tuple field semantics.
 */
public interface Nuple {

    /**
     * Returns the number of addressable fields.
     *
     * @return the number of fields.
     */
    int arity();

    /**
     * Returns the same index passed,
     * if in range,
     * or throws an exception.
     *
     * @param index an index being evaluated
     * @return the same index, or throws
     * @throws IllegalArgumentException on out of range indices
     */
    default int requireInRange(final int index) {
        return Optional.of(index)
                .filter(n -> n < arity())
                .filter(n -> n > -1)
                .orElseThrow(() -> becauseNoMember(index));
    }

    /**
     * Returns the erased class of the item at the given index.
     *
     * @param index an index.
     * @return an item class.
     * @throws IllegalArgumentException on out of range indices
     */
    Class<?> nthRawType(final int index);

    /**
     * Returns the generic class of the item at the given index,
     * as compatible with the expected class.
     *
     * @param index        an index.
     * @param expectedType the expected class.
     * @param <T>          the item type.
     * @return an item class.
     * @throws IllegalArgumentException on out of range indices
     */
    <T> Class<T> nthType(final int index, final Class<T> expectedType);

    /**
     * Returns the generic item at the given index,
     * as compatible with the expected class.
     *
     * @param index        an index.
     * @param expectedType the expected class.
     * @param <T>          the item type.
     * @return an item.
     * @throws IllegalArgumentException on out of range indices
     */
    <T> T nthMember(final int index, final Class<T> expectedType);

    /**
     * Returns the type-erased item at the given index.
     *
     * @param index an index.
     * @return an item.
     * @throws IllegalArgumentException on out of range indices
     */
    Object nthRawMember(final int index);

    /**
     * Returns a new nuple with the given value and inferred types.
     *
     * @param values the values.
     * @return a new arbitrary nuple.
     */
    static Nuple nupleOfValues(final Object... values) {
        return new ArrayNuple(values);
    }

    /**
     * Guesses expected classes for the given values.
     *
     * @param values some values.
     * @return some classes.
     */
    static Class<?>[] inferredTypes(final Object... values) {
        return Stream.of(values)
                .map(defaultingType(Void.class))
                .toArray(Class[]::new);
    }

    /**
     * Returns an exception indicating the index is out of range.
     *
     * @param index an index.
     * @return an exception.
     */
    static IllegalArgumentException becauseNoMember(final int index) {
        return becauseIllegal("no member %s", index);
    }

    /**
     * Returns a new arbitrary nuple.
     *
     * @param values the values.
     * @param types  the types.
     * @return a new nuple.
     */
    static Nuple nupleOfTypedValues(final Object[] values, final Class<?>... types) {
        return new ArrayNuple(values, types);
    }

    /**
     * Factory for arbitrary vs typed nuple values.
     */
    final class NupleFactory {

        private NupleFactory() {
            throw becauseFactory();
        }

        static final class ArrayNuple implements Nuple {

            final Object[] contents;
            final Class<?>[] types;

            @Override
            public int arity() {
                return contents.length;
            }

            public Class<?> nthRawType(final int index) {
                return types[requireInRange(index)];
            }

            @Override
            @SuppressWarnings(SuppressionConstants.ACTUALLY_CHECKED)
            public <T> Class<T> nthType(final int index, final Class<T> expectedType) {
                return (Class<T>) Optional.of(nthRawType(index))
                        .filter(expectedType::equals)
                        .orElse(null);
            }

            @Override
            @SuppressWarnings(SuppressionConstants.ACTUALLY_CHECKED)
            public <T> T nthMember(int index, Class<T> expectedType) {
                return (T) Optional.ofNullable(nthRawMember(index))
                        .filter(expectedType::isInstance)
                        .orElse(null);
            }

            @Override
            public Object nthRawMember(int index) {
                return contents[requireInRange(index)];
            }

            ArrayNuple(Object[] contents, Class<?>... types) {
                if (contents.length == 0) {
                    this.contents = new Object[types.length];
                } else {
                    this.contents = new Object[contents.length];
                    arraycopy(contents, 0, this.contents, 0, contents.length);
                }
                if (types.length == 0) {
                    this.types = inferredTypes(contents);
                } else {
                    this.types = new Class<?>[types.length];
                    arraycopy(types, 0, this.types, 0, types.length);
                }
                sanityCheck();
            }

            private void sanityCheck() {
                if (contents.length != types.length
                        || !IntStream.range(0, contents.length)
                        .filter(n -> nonNull(contents[n]))
                        .allMatch(n -> types[n].isAssignableFrom(contents[n].getClass()))
                ) {
                    throw ExceptionFactory.becauseIllegal("Types %n%s%ndon't match contents%n%s%n",
                            Arrays.toString(types),
                            Arrays.toString(contents));
                }
            }
        }
    }
}
