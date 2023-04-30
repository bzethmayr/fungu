package net.zethmayr.fungu;

record SimpleFork<T, B>(T top, B bottom) implements Fork<T, B> {

    @Override
    public SimpleFork<T, B> with(final T top, final B bottom) {
        return new SimpleFork<>(top, bottom);
    }

}
