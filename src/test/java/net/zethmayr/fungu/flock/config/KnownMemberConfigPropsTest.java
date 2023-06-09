package net.zethmayr.fungu.flock.config;

import net.zethmayr.fungu.fields.WiringHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.function.Consumer;

import static net.zethmayr.fungu.test.TestConstants.TEST_RANDOM;
import static net.zethmayr.fungu.test.TestHelper.randomString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class KnownMemberConfigPropsTest {

    @BeforeAll
    void setup() {
        WiringHelper.wireField(KnownMemberConfigProps.class);
    }

    void randomlyConfigure(final ConfiguresKnownMember underTest) {
        underTest.setInitialValue(TEST_RANDOM.nextLong());
        underTest.setLocation(randomString(5));
        underTest.setIndex(TEST_RANDOM.nextInt(8));
    }

    @Test
    void getFieldCopier_whenSourceValuesConfigured_givenEditingClass_returnsCopier_givenInstance_copiesSourceValues() {
        final ConfiguresKnownMember source = new KnownMemberConfigProps();
        randomlyConfigure(source);
        final ConfiguresKnownMember underTest = new KnownMemberConfigProps();

        assertNull(underTest.getInitialValue());
        assertNull(underTest.getLocation());
        assertNull(underTest.getIndex());
        final Consumer<ConfiguresKnownMember> copier = source.getCopier(ConfiguresKnownMember.class);
        copier.accept(underTest);

        assertEquals(source.getIndex(), underTest.getIndex());
        assertEquals(source.getLocation(), underTest.getLocation());
        assertEquals(source.getInitialValue(), underTest.getInitialValue());
    }
}
