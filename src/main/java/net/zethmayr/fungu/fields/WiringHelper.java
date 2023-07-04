package net.zethmayr.fungu.fields;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static net.zethmayr.fungu.DecisionHelper.anyNull;
import static net.zethmayr.fungu.core.ExceptionFactory.*;
import static net.zethmayr.fungu.core.SuppressionConstants.LOCAL_CONVENTION;
import static net.zethmayr.fungu.fields.EditsX.getCopyFunction;
import static net.zethmayr.fungu.fields.HasX.getGetFunction;
import static net.zethmayr.fungu.fields.SetsX.getSetFunction;

/**
 * Registers field interface supporting instances.
 */
public final class WiringHelper {
    private WiringHelper() {
        throw becauseStaticsOnly();
    }

    /**
     * Finds getter-like methods associated with the given class
     * and provides them to the given handler.
     * Methods are considered getter-like if they
     * take no argument and return a value
     * and either are the only methods for the class
     * or are annotated with {@link Gets}.
     *
     * @param encountered   a class.
     * @param getterHandler the handler.
     * @param <H>           the class type.
     */
    public static <H extends HasX> void findGetters(
            final Class<H> encountered, final BiConsumer<Class<? extends HasX>, Method> getterHandler
    ) {
        // doesn't use caches, at least not directly
        final Method[] candidates = Stream.of(encountered.getMethods())
                .filter(m -> m.getDeclaringClass() == encountered)
                .filter(m -> m.getParameterCount() == 0)
                .filter(m -> !Void.class.equals(m.getReturnType()))
                .toArray(Method[]::new);
        Stream.of(candidates)
                .filter(m -> candidates.length == 1 || nonNull(m.getAnnotation(Gets.class)))
                .forEach(m -> getterHandler.accept(canHas(m.getDeclaringClass()), m));
        Stream.of(encountered.getInterfaces())
                .filter(HasX.class::isAssignableFrom)
                .forEach(i -> findGetters(canHas(i), getterHandler));
    }

    private static <H extends HasX> BiConsumer<Class<? extends H>, Method> wiresGetter(final Lookup permissions) {
        return (c, m) -> HasX.registerGetFunction(c, () -> getterInvoker(convertMethod(m, permissions)));
    }

    /**
     * Wires getters
     * associated with the given class's field interfaces.
     *
     * @param declaring a class.
     * @param <H>       the gettable type.
     */
    public static <H extends HasX> void wireGetters(final Class<H> declaring) {
        final Lookup permissions = MethodHandles.lookup();
        findGetters(declaring, wiresGetter(permissions));
    }

    /**
     * Wires setters
     * associated with the given class's field interfaces.
     *
     * @param declaring a class.
     * @param <S>       the settable type.
     */
    public static <S extends SetsX> void wireSetters(final Class<S> declaring) {
        final Lookup permissions = MethodHandles.lookup();
        final Method[] candidates = Stream.of(declaring.getMethods())
                .filter(m -> m.getDeclaringClass() == declaring)
                .filter(m -> m.getParameterCount() == 1)
                .toArray(Method[]::new);
        Stream.of(candidates)
                .filter(m -> candidates.length == 1 || nonNull(m.getAnnotation(Sets.class)))
                .forEach(m -> {
                    final MethodHandle handle = convertMethod(m, permissions);
                    SetsX.registerSetFunction(canSets(m.getDeclaringClass()), () -> setterInvoker(handle));
                });
        Stream.of(declaring.getInterfaces())
                .filter(SetsX.class::isAssignableFrom)
                .forEach(i -> wireSetters(canSets(i)));
    }

    /**
     * Wires getters, setters, and copiers
     * associated with the given class's field interface(s).
     *
     * @param declaring a class.
     * @param <E>       the editable type.
     */
    public static <E extends EditsX> void wireField(final Class<E> declaring) {
        wireGetters(declaring);
        wireSetters(declaring);
        registerCopiers(declaring);
    }

    @SuppressWarnings(LOCAL_CONVENTION)
    private static <H extends HasX, T> Function<H, T> getterInvoker(final MethodHandle getterHandle) {
        return h -> {
            try {
                return (T) getterHandle.invoke(h);
            } catch (final Throwable t) {
                throw new RuntimeException(t);
            }
        };
    }

    private static <S extends SetsX, T> BiConsumer<S, T> setterInvoker(final MethodHandle setterHandle) {
        return (s, v) -> {
            try {
                setterHandle.invoke(s, v);
            } catch (final Throwable t) {
                throw new RuntimeException(t);
            }
        };
    }

    private static MethodHandle convertMethod(final Method reflected, final Lookup permissions) {
        try {
            return permissions.unreflect(reflected);
        } catch (final IllegalAccessException iae) {
            throw becauseThrewBecauseIllegal(
                    "The method %s was not accessible per %s",
                    iae, reflected, permissions);
        }
    }

    /**
     * Wires copiers for the given class's editable field(s).
     *
     * @param editable   a class.
     * @param fieldClass the field type (optional).
     * @param <E>        the editable type.
     * @param <T>        the field type.
     */
    public static <E extends EditsX, T> void wireCopier(final Class<E> editable, final T fieldClass) {
        registerCopiers(editable);
    }

    private static <E extends EditsX> void registerCopiers(final Class<E> editable) {
        registerCopiers(editable, new EditableStack());
    }

    @SuppressWarnings(LOCAL_CONVENTION)
    private static <H extends HasX> Class<H> canHas(final Class<?> doesHas) {
        return (Class<H>) doesHas;
    }

    @SuppressWarnings(LOCAL_CONVENTION)
    private static <S extends SetsX> Class<S> canSets(final Class<?> doesSets) {
        return (Class<S>) doesSets;
    }

    @SuppressWarnings(LOCAL_CONVENTION)
    private static <E extends EditsX> Class<E> canEdits(final Class<?> doesEdits) {
        return (Class<E>) doesEdits;
    }

    @SuppressWarnings(LOCAL_CONVENTION)
    private static <E> Class<? extends E>[] erased(final int length) {
        return new Class[length];
    }

    /**
     * Registers the copier(s) for a given class's field interface(s).
     *
     * @param visited the class or interface being looked at
     * @param stack   the stack for this recursive call
     */
    private static void registerCopiers(final Class<?> visited, final EditableStack stack) {
        /*
         * We'd like to traverse over each class as few times as possible.
         * Lossy traversals are conveniently restrictive, but get redundant quickly.
         * Consider the following class:
         * <pre>
         * Concretion           <- multiple field copier target
         *    EditsFoo          <- single field copier target (for Concretion)
         *       EditsX         <- marks EditsFoo as copier target
         *          SetsX
         *          HasX
         *       SetsFoo        <- single field setter target (for EditsFoo)
         *          SetsX       <- marks SetsFoo as setter target
         *       HasFoo         <- single field getter target (for EditsFoo)
         *          HasX        <- marks HasFoo as getter target
         *    EditsBar          <- single field copier target (for Concretion)
         *       EditsX         <- marks EditsBar as copier target
         *          SetsX
         *          HasX
         *       SetsBar        <- single field setter target (for EditsBar)
         *          SetsX       <- marks SetsBar as setter target
         *       HasBar         <- single field getter target (for EditsBar)
         *          HasX        <- marks HasBar as getter target
         * </pre>
         * We can assume that shallower classes are seen sooner.
         * We can't assume the encounter order at each level.
         * We also need to ignore potential irrelevant interfaces.
         * <p />
         * We may also see complications -
         * Concretion might also be marked with any of the lower-level markers.
         * Unrelated interfaces may be present
         */
        if (visited == HasX.class) {
            stack.pointed.pokeGettable(stack.pointed.handled);
        } else if (visited == SetsX.class) {
            stack.pointed.pokeSettable(stack.pointed.handled);
        } else if (visited == EditsX.class) {
            stack.pointed.isEditable = true;
        } else {
            final EditingFrame frame = stack.push();
            frame.handled = visited;
            Stream.of(visited.getInterfaces())
                    .forEach(i -> registerCopiers(i, stack));
            frame.isEditable &= frame.isSettable & frame.isGettable;
            if (frame.fieldInterfaces.getGettable().size() > 0) {
                if (frame.isGettable && nonNull(frame.parent)) {
                    frame.parent.pokeGettable(frame.handled);
                }
                wireGetters(canHas(frame.handled));
            }
            if (frame.fieldInterfaces.getSettable().size() > 0) {
                if (frame.isSettable && nonNull(frame.parent)) {
                    frame.parent.pokeSettable(frame.handled);
                }
                wireSetters(canSets(frame.handled));
            }
            if (frame.isEditable) {
                EditsX.registerCopyFunction(
                        canEdits(frame.handled),
                        () -> createSingleFieldCopier(canEdits(frame.handled),
                                frame.fieldInterfaces.getGettable().get(0),
                                frame.fieldInterfaces.getSettable().get(0),
                                null)
                );
                if (nonNull(frame.parent)) {
                    frame.parent.pokeEditable(frame.handled);
                }
            } else {
                final List<Class<? extends EditsX>> subeditors = frame.fieldInterfaces.getEditable();
                if (subeditors.size() > 0) {
                    EditsX.registerCopyFunction(
                            canEdits(frame.handled),
                            () -> createMultiFieldCopier(canEdits(frame.handled),
                                    subeditors.toArray(WiringHelper::erased)
                            )
                    );
                }
            }
            stack.pop();
        }
    }

    private static <E extends EditsX, T> BiConsumer<E, E> createSingleFieldCopier(
            final Class<E> editable,
            final Class<?> having,
            final Class<?> setting,
            final Class<T> fieldClass
    ) {
        //TODO decide what to do about type stricture
        final Function<E, T> getter = getGetFunction(canHas(having), fieldClass);
        final BiConsumer<E, T> setter = getSetFunction(canSets(setting), fieldClass);
        if (anyNull(getter, setter)) {
            throw becauseIllegal("Could not create copier for %s - getter %s, setter %s",
                    editable, getter, setter);
        }
        return (E source, E target) -> setter.accept(target, getter.apply(source));
    }

    @SuppressWarnings(LOCAL_CONVENTION)
    private static <E extends EditsX> BiConsumer<E, E> createMultiFieldCopier(
            final Class<E> editable, final Class<? extends EditsX>... subeditors
    ) {
        requireNonNull(editable);
        // this either depends on _or_ works in spite of type erasure... not sure which :|
        return (BiConsumer<E, E>) Stream.of(subeditors)
                .map(e -> getCopyFunction(e, null))
                .reduce((a, b) -> (s, t) -> {
                    a.accept(s, t);
                    b.accept(s, t);
                })
                .orElse(null);
    }

    private static class EditingFrame {
        public final EditingFrame parent;

        public boolean isEditable;
        public boolean isSettable;
        public boolean isGettable;

        public Class<?> handled;

        void pokeGettable(final Class<?> doesHas) {
            fieldInterfaces.addGettable(canHas(doesHas));
            isGettable = 1 == fieldInterfaces.getGettable().size();
        }

        void pokeSettable(final Class<?> doesSets) {
            fieldInterfaces.addSettable(canSets(doesSets));
            isSettable = 1 == fieldInterfaces.getSettable().size();
        }

        void pokeEditable(final Class<?> doesEdits) {
            fieldInterfaces.addEditable(canEdits(doesEdits));
        }

        public final FieldInterfaces fieldInterfaces = new FieldInterfaces();

        public EditingFrame(final EditingFrame parent) {
            this.parent = parent;
        }
    }

    private static class EditableStack {

        public EditingFrame pointed = null;

        public EditingFrame push() {
            pointed = new EditingFrame(pointed);
            return pointed;
        }

        public void pop() {
            pointed = pointed.parent;
        }
    }

    private static final class FieldInterfaces {
        private final List<Class<? extends HasX>> gettable = new ArrayList<>();
        private final List<Class<? extends SetsX>> settable = new ArrayList<>();

        private final List<Class<? extends EditsX>> editable = new ArrayList<>();

        void addGettable(final Class<? extends HasX> gettable) {
            this.gettable.add(gettable);
        }

        void addSettable(final Class<? extends SetsX> settable) {
            this.settable.add(settable);
        }

        void addEditable(final Class<? extends EditsX> editable) {
            this.editable.add(editable);
        }

        List<Class<? extends HasX>> getGettable() {
            return gettable;
        }

        List<Class<? extends SetsX>> getSettable() {
            return settable;
        }

        List<Class<? extends EditsX>> getEditable() {
            return editable;
        }
    }
}
