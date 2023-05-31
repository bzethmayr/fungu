package net.zethmayr.fungu.capabilities;

import net.zethmayr.fungu.fields.Sets;
import net.zethmayr.fungu.fields.SetsX;
import org.jetbrains.annotations.NotNull;

import java.net.URI;

public interface SetsLocation extends SetsX {

    @Sets
    void setLocation(final String location);

    default void setLocation(@NotNull final URI location) {
        setLocation(location.toString());
    }
}
