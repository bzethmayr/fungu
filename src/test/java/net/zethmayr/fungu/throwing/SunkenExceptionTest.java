package net.zethmayr.fungu.throwing;

import org.junit.jupiter.api.Test;

import static net.zethmayr.fungu.throwing.SunkenException.becauseSunken;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;

class SunkenExceptionTest {

    @Test
    void becauseSunken_givenMultiplyWrappedCause_whenUnwrapped_returnsCause() {
        final RuntimeException thrown = new RuntimeException();


        final SunkenException underTest = becauseSunken(becauseSunken(becauseSunken(thrown)));

        assertThat(underTest.getCause(), sameInstance(thrown));
    }
}