package net.zethmayr.fungu.capabilities;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static net.zethmayr.fungu.test.TestConstants.SHIBBOLETH;
import static org.junit.jupiter.api.Assertions.*;

class HasLocationTest {

    @Test
    void getLocationUri_whenNoLocation_throwsNullPointerException() {
        final HasLocation underTest = new TestEditsLocation();

        assertThrows(NullPointerException.class,

                underTest::getLocationUri);
    }

    @Test
    void getLocationUri_whenInvalidLocation_throwsUriSyntaxException() {
        final EditsLocation underTest = new TestEditsLocation();
        underTest.setLocation(":/");

        assertThrows(URISyntaxException.class,

                underTest::getLocationUri);
    }

    @Test
    void getLocationUri_whenValidLocation_returnsExpectedUri() {
        final EditsLocation underTest = new TestEditsLocation();
        final String uriString = "http://localhost:8080/";
        underTest.setLocation(uriString);

        final URI uri = assertDoesNotThrow(underTest::getLocationUri);

        assertEquals(uriString, uri.toString());
    }
}
