package net.zethmayr.fungu.fields;

import java.util.UUID;

import static net.zethmayr.fungu.test.TestConstants.UNEXPECTED;

public class TestEditsQuux implements EditsQuux {

    private UUID quux;

    public TestEditsQuux(final UUID quux) {
        this.quux = quux;
    }

    public TestEditsQuux() {
        this(UUID.nameUUIDFromBytes(UNEXPECTED.getBytes()));
    }

    @Override
    public UUID getQuux() {
        return quux;
    }

    @Override
    public void setQuux(final UUID quux) {
        this.quux = quux;
    }
}
