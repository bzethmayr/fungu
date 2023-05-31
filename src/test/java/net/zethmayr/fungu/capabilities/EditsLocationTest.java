package net.zethmayr.fungu.capabilities;

import net.zethmayr.fungu.fields.WiringHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.function.Consumer;

import static net.zethmayr.fungu.test.TestConstants.SHIBBOLETH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class EditsLocationTest {

    @BeforeAll
    void setup() {
        WiringHelper.wireField(TestEditsLocation.class);
    }

    @Test
    void getFieldCopier_whenSetLocation_givenFieldInterface_givenNewInstance_copiesLocation() {
        final EditsLocation underTest = new TestEditsLocation();
        underTest.setLocation(SHIBBOLETH);
        final EditsLocation target = new TestEditsLocation();

        final Consumer<EditsLocation> copier = underTest.getFieldCopier(EditsLocation.class, String.class);
        copier.accept(target);

        assertEquals(SHIBBOLETH, target.getLocation());
    }
}
