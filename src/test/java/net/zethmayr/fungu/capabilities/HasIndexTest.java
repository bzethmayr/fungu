package net.zethmayr.fungu.capabilities;

import net.zethmayr.fungu.fields.WiringHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.function.Supplier;

import static net.zethmayr.fungu.test.TestConstants.TEST_RANDOM;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class HasIndexTest {

    @BeforeAll
    void setup() {
        WiringHelper.wireGetters(TestHasIndex.class);
    }

    @Test
    void getIndex_givenNothing_returnsIndex() {
        final int expected = TEST_RANDOM.nextInt();
        final HasIndex underTest = new TestHasIndex(expected);

        assertEquals(expected, underTest.getIndex());
    }

    @Test
    void getGetter_givenInterfaceAndFieldTypes_returnsGetter() {
        final int expected = TEST_RANDOM.nextInt();
        final HasIndex underTest = new TestHasIndex(expected);
        final Supplier<Integer> getter = underTest.getGetter(HasIndex.class, Integer.class);

        assertEquals(expected, getter.get());
    }
}
