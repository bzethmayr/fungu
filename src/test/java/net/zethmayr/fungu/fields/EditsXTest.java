package net.zethmayr.fungu.fields;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.function.Consumer;

import static net.zethmayr.fungu.test.TestConstants.EXPECTED;
import static net.zethmayr.fungu.test.TestConstants.SHIBBOLETH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class EditsXTest {

    @BeforeAll
    static void wire() {
        WiringHelper.wireCopier(TestEditsBoth.class, null);
        WiringHelper.wireCopier(TestEditsQuux.class, null);
    }

    @Test
    void getFieldCopier_whenCopierRegistered_whenInstanceHasExpected_givenSimpleInterface_returnsCopierCopyingValue() {
        final EditsFoo simple = new TestEditsFoo(EXPECTED);
        final EditsFoo target = new TestEditsFoo();

        assertNotEquals(EXPECTED, target.getFoo());
        final Consumer<EditsFoo> underTest = simple.getFieldCopier(EditsFoo.class, String.class);
        underTest.accept(target);

        assertEquals(EXPECTED, target.getFoo());
    }

    @Test
    void getFieldCopier_whenCopierRegistered_whenInstanceHasShibboleth_givenSimpleInterface_returnsCopierCopyingValue() {
        final EditsBar simple = new TestEditsBoth();
        simple.setBar(SHIBBOLETH);
        final EditsBar target = new TestEditsBoth();

        assertNotEquals(SHIBBOLETH, target.getBar());
        final Consumer<EditsBar> underTest = simple.getFieldCopier(EditsBar.class, String.class);
        underTest.accept(target);

        assertEquals(SHIBBOLETH, target.getBar());
    }

    @Test
    void getCopier_whenCopiersRegistered_givenTwoFieldClass_returnsCopier_copierCopiesBothValues() {
        final TestEditsBoth complex = new TestEditsBoth();
        complex.setFoo(EXPECTED);
        complex.setBar(SHIBBOLETH);
        final TestEditsBoth target = new TestEditsBoth();

        assertNotEquals(EXPECTED, target.getFoo());
        assertNotEquals(SHIBBOLETH, target.getBar());
        final Consumer<TestEditsBoth> underTest = complex.getCopier(TestEditsBoth.class);
        underTest.accept(target);

        assertEquals(EXPECTED, target.getFoo());
        assertEquals(SHIBBOLETH, target.getBar());
    }

    @Test
    void getFieldCopier_whenCopiersRegistered_givenSingleFieldClasses_returnsCopiers_copiersCopySeparateValues() {
        final TestEditsFoo editsFoo = new TestEditsFoo(EXPECTED);
        final EditsBar editsBar = new TestEditsBoth();
        editsBar.setBar(SHIBBOLETH);
        final TestEditsBoth target = new TestEditsBoth();

        assertNotEquals(EXPECTED, target.getFoo());
        assertNotEquals(SHIBBOLETH, target.getBar());
        final Consumer<EditsFoo> copiesFoo = editsFoo.getFieldCopier(EditsFoo.class, String.class);
        final Consumer<EditsBar> copiesBar = editsBar.getFieldCopier(EditsBar.class, String.class);
        copiesFoo.accept(target);
        assertNotEquals(SHIBBOLETH, target.getBar());
        copiesBar.accept(target);

        assertEquals(EXPECTED, target.getFoo());
        assertEquals(SHIBBOLETH, target.getBar());
    }

    @Test
    void getCopier_whenDefiningInterfaceIsCombined_givenFieldClass_returnsCopier_copierCopiesValue() {
        final UUID expected = UUID.randomUUID();
        final TestEditsQuux source = new TestEditsQuux(expected);
        final TestEditsQuux target = new TestEditsQuux();

        assertNotEquals(expected, target.getQuux());
        final Consumer<EditsQuux> copiesQuux = source.getCopier(EditsQuux.class);
        copiesQuux.accept(target);

        assertEquals(expected, target.getQuux());
    }
}
