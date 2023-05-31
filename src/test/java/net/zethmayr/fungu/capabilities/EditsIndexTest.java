package net.zethmayr.fungu.capabilities;

import net.zethmayr.fungu.fields.WiringHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.function.Consumer;

import static net.zethmayr.fungu.test.TestConstants.TEST_RANDOM;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class EditsIndexTest {

    @BeforeAll
    void setup() {
        WiringHelper.wireField(TestEditsIndex.class);
    }

    @Test
    void getAndSet_givenIndex_givenNothing_returnsIndex() {
        final int expected = TEST_RANDOM.nextInt();
        final EditsIndex underTest = new TestEditsIndex();

        underTest.setIndex(expected);

        assertEquals(expected, underTest.getIndex());
    }

    @Test
    void setAndGet_givenString_givenNothing_returnsIndex() {
        final int expected = TEST_RANDOM.nextInt();
        final String external = String.format("%d", expected);
        final EditsIndex underTest = new TestEditsIndex();

        underTest.setIndex(external);

        assertEquals(expected, underTest.getIndex());
    }

    @Test
    void getCopier_whenIndex_givenNothing_whenNewInstance_copiesIndex() {
        final int expected = TEST_RANDOM.nextInt();
        final EditsIndex underTest = new TestEditsIndex();
        underTest.setIndex(expected);
        final EditsIndex target = new TestEditsIndex();

        final Consumer<EditsIndex> copier = underTest.getFieldCopier(EditsIndex.class, Integer.class);
        copier.accept(target);

        assertEquals(expected, target.getIndex());
    }
}
