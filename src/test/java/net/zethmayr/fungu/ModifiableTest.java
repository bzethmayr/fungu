package net.zethmayr.fungu;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ModifiableTest {

    final static class TestModifiable implements Modifiable<TestModifiable> {
        private int foo;
        private int bar;

        private AtomicInteger turnCounter = new AtomicInteger();

        public TestModifiable(final int foo, final int bar) {
            this.foo = foo;
            this.bar = bar;
        }

        private int bonk() {
            return turnCounter.getAndIncrement();
        }

        private boolean wasFooTurn() {
            return bonk() % 2 == 0;
        }

        public void set(final int value) {
            if (wasFooTurn()) {
                foo = value;
            } else {
                bar = value;
            }
        }

        public int get() {
            return wasFooTurn()
                    ? foo
                    : bar;
        }

        public void compute() {
            final int shift = bonk() % 17;
            final int quux = foo >> shift + bar << shift;
            foo = foo << shift + bar >> shift;
            bar = quux;
        }
    }

    @Test
    void modified_whenChained_returnsFinalResult() {
        final Consumer<TestModifiable> setTwo = m -> m.set(2);
        final TestModifiable underTest = new TestModifiable(1, 1)
                .modified(setTwo)
                .modified(setTwo)
                .modified(TestModifiable::compute);

        assertNotEquals(2, underTest.get());
        assertNotEquals(2, underTest.get());
    }

    @Test
    void modified_whenConsumer_returnsModifiedInstance() {
        final TestModifiable underTest = new TestModifiable(0, 1);
        assertEquals(0, underTest.get());
        assertEquals(1, underTest.get());

        assertEquals(underTest, underTest.modified(m -> m.set(1)));
        assertEquals(1, underTest.get());
        assertEquals(1, underTest.get());
    }

    @Test
    void modified_whenSelfConsumer_returnsModifiedInstance() {
        final TestModifiable underTest = new TestModifiable(1, 1);

        assertEquals(underTest, underTest.modified(TestModifiable::compute));
        assertNotEquals(1, underTest.get());
        assertNotEquals(1, underTest.get());
    }
}
