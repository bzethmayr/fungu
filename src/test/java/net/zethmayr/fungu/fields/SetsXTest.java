package net.zethmayr.fungu.fields;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static net.zethmayr.fungu.core.SupplierFactory.from;
import static net.zethmayr.fungu.test.TestConstants.EXPECTED;
import static net.zethmayr.fungu.test.TestConstants.UNEXPECTED;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SetsXTest {

    private static class TestSetsFoo implements SetsFoo {
        public String foo = UNEXPECTED;

        public void setFoo(final String foo) {
            this.foo = foo;
        }
    }

    private static class TestSetsBar implements SetsBar {

        public String bar = UNEXPECTED;

        public void setBar(final String bar) {
            this.bar = bar;
        }
    }

    private static class TestComposite implements SetsFoo, SetsBar {
        public String foo = UNEXPECTED;
        public String bar = UNEXPECTED;

        public void setFoo(final String foo) {
            this.foo = foo;
        }

        public void setBar(final String bar) {
            this.bar = bar;
        }
    }

    @BeforeAll
    static void wire() {
        SetsX.registerSetFunction(SetsFoo.class, () -> SetsFoo::setFoo);
        SetsX.registerSetFunction(SetsBar.class, () -> SetsBar::setBar);
    }

    @Test
    void getSetter_whenSimpleHavingFoo_givenFooTypes_givenValue_acceptsValue() {
        final TestSetsFoo underTest = new TestSetsFoo();

        final Consumer<String> setter = underTest.getSetter(SetsFoo.class, String.class);
        setter.accept(EXPECTED);

        Assertions.assertEquals(EXPECTED, underTest.foo);
    }

    @Test
    void getSetter_whenCompositeHavingFoo_givenFooTypes_givenValue_acceptsValue() {
        final TestComposite underTest = new TestComposite();

        final Consumer<String> setter = underTest.getSetter(SetsFoo.class, String.class);
        setter.accept(EXPECTED);

        Assertions.assertEquals(EXPECTED, underTest.foo);
    }

    @Test
    void getSetter_whenSimpleSetsBar_givenBarTypes_givenValue_acceptsValue() {
        final TestSetsBar underTest = new TestSetsBar();

        final Consumer<String> setter = underTest.getSetter(SetsBar.class, String.class);
        setter.accept(EXPECTED);

        Assertions.assertEquals(EXPECTED, underTest.bar);
    }

    @Test
    void getSetter_whenCompositeSetsBar_givenBarTypes_givenValue_acceptsValue() {
        final TestComposite underTest = new TestComposite();

        final Consumer<String> setter = underTest.getSetter(SetsBar.class, String.class);
        setter.accept(EXPECTED);

        Assertions.assertEquals(EXPECTED, underTest.bar);
    }

    @Test
    void getSetFunction_givenFooTypes_givenSimpleInstanceAndValue_acceptsValue() {
        final TestSetsFoo underTest = new TestSetsFoo();

        final BiConsumer<SetsFoo, String> setFunction = SetsX.getSetFunction(SetsFoo.class, String.class);
        setFunction.accept(underTest, EXPECTED);
        
        Assertions.assertEquals(EXPECTED, underTest.foo);
    }

    @Test
    void getSetFunction_givenFooTypes_givenCompositeInstanceAndValue_acceptsValue() {
        final TestComposite underTest = new TestComposite();

        final BiConsumer<SetsFoo, String> setFunction = SetsX.getSetFunction(SetsFoo.class, String.class);
        setFunction.accept(underTest, EXPECTED);
        
        Assertions.assertEquals(EXPECTED, underTest.foo);
    }

    @Test
    void getSetFunction_givenBarTypes_givenSimpleInstanceAndValue_acceptsValue() {
        final TestSetsBar underTest = new TestSetsBar();

        final BiConsumer<SetsBar, String> setFunction = SetsX.getSetFunction(SetsBar.class, String.class);
        setFunction.accept(underTest, EXPECTED);
        
        Assertions.assertEquals(EXPECTED, underTest.bar);
    }

    @Test
    void compositeClassHavingBar_whenGetSetFunction_givenInstance_acceptsValue() {
        final TestComposite underTest = new TestComposite();

        final BiConsumer<SetsBar, String> setFunction = SetsX.getSetFunction(SetsBar.class, String.class);
    setFunction.accept(underTest, EXPECTED);

        Assertions.assertEquals(EXPECTED, underTest.bar);
    }
}
