package net.zethmayr.fungu.capabilities;

import net.zethmayr.fungu.fields.Gets;
import net.zethmayr.fungu.fields.HasX;

import java.net.URI;
import java.net.URISyntaxException;

public interface HasLocation extends HasX {

    @Gets
    String getLocation();

    default URI getLocationUri() throws URISyntaxException {
        return new URI(getLocation());
    }
}
