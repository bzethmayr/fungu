package net.zethmayr.fungu;

import net.zethmayr.fungu.test.Irrelevant;
import org.junit.jupiter.api.Test;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static net.zethmayr.fungu.UponHelper.throwingUpon;
import static net.zethmayr.fungu.UponHelper.upon;
import static net.zethmayr.fungu.test.MatcherFactory.has;
import static net.zethmayr.fungu.test.TestConstants.EXPECTED;
import static net.zethmayr.fungu.test.TestConstants.UNEXPECTED;
import static net.zethmayr.fungu.test.TestHelper.invokeDefaultConstructor;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.*;

class UponHelperTest {

    @Test
    void uponHelper_whenInstantiated_throwsInstead() {
        assertThrows(UnsupportedOperationException.class, () ->

                invokeDefaultConstructor(UponHelper.class));
    }

    @Test
    void upon_givenOnlyInstance_returnsInstance() {
        final Irrelevant target = new Irrelevant();

        assertSame(target, upon(target));
    }

    @Test
    void upon_givenInstanceAndVisitor_returnsInstanceAfterVisit() {
        final List<Irrelevant> exampleStructure = new ArrayList<>();
        final Consumer<Irrelevant> visitor = exampleStructure::add;
        final Irrelevant target = new Irrelevant();

        assertSame(target, upon(target, visitor));

        assertThat(exampleStructure, contains(target));
    }

    @Test
    void upon_givenInstanceVisitorAndMutator_returnsInstanceAfterVisitAndMutation() {
        final List<Irrelevant> exampleStructure = new ArrayList<>();
        final Consumer<Irrelevant> mutator = t -> t.setField(EXPECTED);
        final Irrelevant target = new Irrelevant(UNEXPECTED);

        assertSame(target, upon(target, exampleStructure::add, mutator));

        assertThat(exampleStructure, contains(target));
        assertThat(target, has(Irrelevant::getField, EXPECTED));
    }

    @Test
    void throwingUpon_givenOnlyInstance_returnsInstance() {

        assertSame(EXPECTED, throwingUpon(EXPECTED));
    }

    @Test
    void throwingUpon_givenInstanceAndMutator_returnsInstanceAfterMutation() {
        final Testable target = new TestResource();

        assertSame(target, assertDoesNotThrow(() ->
                throwingUpon(target, Closeable::close)));

        assertTrue(target.isClosed());
    }

    @Test
    void throwingUpon_givenInstanceMutatorAndVisitor_returnsInstanceAfterMutationAndVisit() {
        final List<Closeable> exampleStructure = new ArrayList<>();
        final Testable target = new TestResource();

        assertSame(target, assertDoesNotThrow(() ->
                throwingUpon(target, Closeable::close, exampleStructure::add)));

        assertTrue(target.isClosed());
        assertThat(exampleStructure, contains(target));
    }
}
