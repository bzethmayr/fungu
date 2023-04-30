package net.zethmayr.fungu.fields;

import java.util.UUID;

public interface EditsQuux extends HasX, SetsX, EditsX {
    UUID getQuux();

    void setQuux(final UUID quux);
}
