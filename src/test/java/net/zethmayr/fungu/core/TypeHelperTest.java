package net.zethmayr.fungu.core;

import net.zethmayr.fungu.test.Irrelevant;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static net.zethmayr.fungu.core.TypeHelper.defaultingType;
import static net.zethmayr.fungu.core.TypeHelper.typeOrDefault;
import static net.zethmayr.fungu.test.TestHelper.invokeDefaultConstructor;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TypeHelperTest {

    @Test
    void typeHelper_whenInstantiated_throwsInstead() {

        assertThrows(UnsupportedOperationException.class, () ->
                invokeDefaultConstructor(TypeHelper.class));
    }

    @Test
    void typeOrDefault_givenInstanceAndDefault_returnsInstanceClass() {
        final Irrelevant instance = new Irrelevant();

        assertThat(typeOrDefault(instance, TypeHelper.class),
                is(Irrelevant.class));
    }

    @Test
    void typeOrDefault_givenNullAndDefault_returnsDefaultClass() {

        assertThat(typeOrDefault(null, Void.class),
                is(Void.class));
    }

    @Test
    void defaultingType_givenDefault_mapsNullsToDefault() {

        assertTrue(Stream.of(new Irrelevant(), null)
                .map(defaultingType(Irrelevant.class))
                .allMatch(c -> c == Irrelevant.class));
    }
}