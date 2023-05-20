package net.zethmayr.fungu;

record SimpleTypedFork<T, B>(Class<T> topType, T top, Class<B> bottomType, B bottom) implements TypedFork<T, B> {

    @Override
    public SimpleTypedFork<T, B> with(final T top, final B bottom) {
        return new SimpleTypedFork<>(topType, top, bottomType, bottom);
    }
}
