package net.zethmayr.fungu;

record TypedFork<T, B>(Class<T> topType, T top, Class<B> bottomType, B bottom) implements ReFork<T, B> {

    @Override
    public TypedFork<T, B> with(final T top, final B bottom) {
        return new TypedFork<>(topType, top, bottomType, bottom);
    }
}
