package net.zethmayr.fungu.capabilities;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SetLocationTest {

    @Test
    void setLocationUri_givenNullUri_throws() {
        final SetsLocation underTest = new TestEditsLocation();

        assertThrows(NullPointerException.class, () ->

                underTest.setLocation((URI) null));
    }

    @Test
    void setLocationUri_givenValidUri_setsUriLocation() throws URISyntaxException {
        final EditsLocation underTest = new TestEditsLocation();
        final String uriString = "http://localhost/";
        final URI uri = new URI(uriString);

        underTest.setLocation(uri);

        assertEquals(uriString, underTest.getLocation());
    }
}
