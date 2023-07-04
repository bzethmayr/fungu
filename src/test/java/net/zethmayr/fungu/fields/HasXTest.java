package net.zethmayr.fungu.fields;

import net.zethmayr.fungu.core.SupplierFactory;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.function.Function;
import java.util.function.Supplier;

import static net.zethmayr.fungu.core.SupplierFactory.from;
import static net.zethmayr.fungu.test.TestConstants.EXPECTED;
import static net.zethmayr.fungu.test.TestConstants.UNEXPECTED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HasXTest {
    /*
     * Some warts to this approach,
     * - the registration can't happen directly at the interface level
     *   - because interfaces can't have static initializers
     *   - nothing is required to refer to a contained initializing class
     *   - interface fields don't initialize until first reference if not compile constant
     *   - field interface methods collide if we specify a "get registered" method
     * - the registration is cluttered and redundant at the instance level
     *   - you won't know which one is actually accomplishing the registration
     *     - and removing the registration code from any given class probably won't break anything
     * - the registration isn't automatic in any way with a central wiring
     *   - but it's the least bad approach.
     * - Annotation processing could help here.
     */

    private static class TestHasBar implements HasBar {
        public TestHasBar(final String bar) {
            this.bar = bar;
        }

        public TestHasBar() {
            this(UNEXPECTED);
        }

        private String bar;

        public String getBar() {
            return bar;
        }
    }

    private static class TestComposite implements HasFoo, HasBar {
        public TestComposite(final String foo, final String bar) {
            this.foo = foo;
            this.bar = bar;
        }

        public TestComposite(final String foo) {
            this(foo, UNEXPECTED);
        }

        public TestComposite() {
            this(UNEXPECTED);
        }

        private String foo;
        private String bar;

        public String getFoo() {
            return foo;
        }

        public String getBar() {
            return bar;
        }
    }

    @BeforeAll
    static void wire() {
        HasX.registerGetFunction(HasFoo.class, SupplierFactory.from(HasFoo::getFoo));
        HasX.registerGetFunction(HasBar.class, SupplierFactory.from(HasBar::getBar));
    }

    @Test
    void getGetter_whenSimpleHasFoo_givenFooTypes_getterSuppliesValue() {
        final HasFoo simple = new TestHasFoo(EXPECTED);

        final Supplier<String> getter = simple.getGetter(HasFoo.class, String.class);

        Assertions.assertEquals(EXPECTED, getter.get());
    }

    @Test
    void getGetter_whenCompositeHasFoo_givenFooTypes_getterSuppliesValue() {
        final HasFoo composite = new TestComposite(EXPECTED);

        final Supplier<String> getter = composite.getGetter(HasFoo.class, String.class);

        Assertions.assertEquals(EXPECTED, getter.get());
    }

    @Test
    void getGetter_whenSimpleHasBar_givenFooTypes_getterSuppliesValue() {
        final HasBar simple = new TestHasBar(EXPECTED);

        final Supplier<String> getter = simple.getGetter(HasBar.class, String.class);

        Assertions.assertEquals(EXPECTED, getter.get());
    }

    @Test
    void getGetter_whenCompositeHasBar_givenFooTypes_getterSuppliesValue() {
        final HasBar composite = new TestComposite(UNEXPECTED, EXPECTED);

        final Supplier<String> getter = composite.getGetter(HasBar.class, String.class);

        Assertions.assertEquals(EXPECTED, getter.get());
    }

    @Test
    void getGetFunction_givenFooTypes_givenSimpleInstance_returnsValue() {
        final HasFoo simple = new TestHasFoo(EXPECTED);

        final Function<HasFoo, String> getFunction = HasX.getGetFunction(HasFoo.class, String.class);

        Assertions.assertEquals(EXPECTED, getFunction.apply(simple));
    }

    @Test
    void getGetFunction_givenFooTypes_givenCompositeInstance_returnsValue() {
        final HasFoo composite = new TestComposite(EXPECTED);

        final Function<HasFoo, String> getFunction = HasX.getGetFunction(HasFoo.class, String.class);

        Assertions.assertEquals(EXPECTED, getFunction.apply(composite));
    }

    @Test
    void getGetFunction_givenBarTypes_givenSimpleInstance_returnsValue() {
        final HasBar simple = new TestHasBar(EXPECTED);

        final Function<HasBar, String> getFunction = HasX.getGetFunction(HasBar.class, String.class);

        Assertions.assertEquals(EXPECTED, getFunction.apply(simple));
    }

    @Test
    void getGetFunction_givenBarTypes_givenCompositeInstance_returnsValue() {
        final HasBar composite = new TestComposite(UNEXPECTED, EXPECTED);

        final Function<HasBar, String> getFunction = HasX.getGetFunction(HasBar.class, String.class);

        Assertions.assertEquals(EXPECTED, getFunction.apply(composite));
    }

}
