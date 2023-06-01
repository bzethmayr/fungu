package net.zethmayr.fungu.capabilities;

import net.zethmayr.fungu.fields.Gets;
import net.zethmayr.fungu.fields.HasX;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Has a readable location field,
 * representable as a URI.
 */
public interface HasLocation extends HasX {

    /**
     * Returns the configured location value.
     * @return the location.
     */
    @Gets
    String getLocation();

    /**
     * Returns the configured location as a URI.
     * @return the location as a URI.
     * @throws URISyntaxException if the location is not valid.
     */
    default URI getLocationUri() throws URISyntaxException {
        return new URI(getLocation());
    }
}
