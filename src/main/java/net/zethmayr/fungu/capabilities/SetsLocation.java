package net.zethmayr.fungu.capabilities;

import net.zethmayr.fungu.fields.Sets;
import net.zethmayr.fungu.fields.SetsX;
import org.jetbrains.annotations.NotNull;

import java.net.URI;

/**
 * Can accept a location,
 * representable as a URI.
 */
public interface SetsLocation extends SetsX {

    /**
     * Sets a new location string.
     *
     * @param location the location.
     */
    @Sets
    void setLocation(final String location);

    /**
     * Sets a new location string from a URI.
     *
     * @param location the location as a URI.
     */
    default void setLocation(@NotNull final URI location) {
        setLocation(location.toString());
    }
}
