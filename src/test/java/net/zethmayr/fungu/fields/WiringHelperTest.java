package net.zethmayr.fungu.fields;

import net.zethmayr.fungu.test.TestConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static net.zethmayr.fungu.fields.WiringHelper.*;
import static net.zethmayr.fungu.test.TestConstants.EXPECTED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class WiringHelperTest {

    @Test
    void wireGetters_whenGetterFromHasSingle_wiresGetterByHandle() {
        wireGetters(HasFoo.class);

        final EditsFoo underTest = new TestEditsFoo();
        final Supplier<String> getter = underTest.getGetter(HasFoo.class, String.class);
        underTest.setFoo(EXPECTED);

        assertNotNull(getter);
        Assertions.assertEquals(EXPECTED, getter.get());
    }

    @Test
    void wireGetters_whenGetterFromEditsSingle_wiresHasGetterByHandle() {
        /*
         * This is a subclass of HasFoo - specific support was needed to ascend to the actual field interfaces
         */
        wireGetters(EditsFoo.class);

        final EditsFoo underTest = new TestEditsFoo();
        final Supplier<String> getter = underTest.getGetter(HasFoo.class, String.class);
        underTest.setFoo(EXPECTED);

        assertNotNull(getter);
        Assertions.assertEquals(EXPECTED, getter.get());
    }

    @Test
    void wireSetters_whenSetterFromSetsSingle_wiresSetterByHandle() {
        wireSetters(SetsFoo.class);

        final EditsFoo underTest = new TestEditsFoo();
        final Consumer<String> setter = underTest.getSetter(SetsFoo.class, String.class);
        setter.accept(EXPECTED);

        assertEquals(EXPECTED, underTest.getFoo());
    }

    @Test
    void wireSetters_whenSetterFromEditsSingle_wiresSetsSetterByHandle() {
        wireSetters(EditsFoo.class);

        final EditsFoo underTest = new TestEditsFoo();
        final Consumer<String> setter = underTest.getSetter(SetsFoo.class, String.class);
        setter.accept(EXPECTED);

        assertEquals(EXPECTED, underTest.getFoo());
    }

    @Test
    void wireField_whenEditsSingle_wiresHasGetterAndSetsSetter() {
        wireField(EditsFoo.class);

        final EditsFoo underTest = new TestEditsFoo();
        final Consumer<String> setter = underTest.getSetter(SetsFoo.class, String.class);
        final Supplier<String> getter = underTest.getGetter(HasFoo.class, String.class);
        setter.accept(EXPECTED);

        Assertions.assertEquals(EXPECTED, getter.get());
    }

    @Test
    void wireField_whenEditsMultiple_wiresGettersAndSetters() {
        wireField(TestEditsBoth.class);

        final TestEditsBoth underTest = new TestEditsBoth();
        final Consumer<String> fooSetter = underTest.getSetter(SetsFoo.class, String.class);
        final Consumer<String> barSetter = underTest.getSetter(SetsBar.class, String.class);
        final Supplier<String> fooGetter = underTest.getGetter(HasFoo.class, String.class);
        final Supplier<String> barGetter = underTest.getGetter(HasBar.class, String.class);
        fooSetter.accept(EXPECTED);
        barSetter.accept(EXPECTED);

        Assertions.assertEquals(EXPECTED, fooGetter.get());
        Assertions.assertEquals(EXPECTED, barGetter.get());
    }
}
