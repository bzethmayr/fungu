package net.zethmayr.fungu.capabilities;

public final class TestHasIndex implements HasIndex {

    final int index;

    TestHasIndex(final int index) {
        this.index = index;
    }

    @Override
    public Integer getIndex() {
        return index;
    }
}
