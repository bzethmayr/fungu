package net.zethmayr.fungu.core;

import static net.zethmayr.fungu.core.ExceptionFactory.becauseConstantsOnly;
import static net.zethmayr.fungu.core.SuppressionConstants.LiteralValues.UNCHECKED;
import static net.zethmayr.fungu.core.SuppressionConstants.LiteralValues.UNUSED;

/**
 * Defines constants used to document uses of {@link SuppressWarnings}.
 */
public class SuppressionConstants {

    private SuppressionConstants() {
        throw becauseConstantsOnly();
    }

    static final class LiteralValues {

        private LiteralValues() {
            throw becauseConstantsOnly();
        }

        static final String UNCHECKED = "unchecked";

        static final String UNUSED = "unused";
    }

    /**
     * This cast is strictly checked, but the check is done dynamically.
     */
    public static final String ACTUALLY_CHECKED = UNCHECKED;

    /**
     * This cast will always succeed, the check is irrelevant.
     */
    public static final String CHECK_ASSURED = UNCHECKED;

    /**
     * This cast is only assured by local convention -
     * this code should be private.
     */
    public static final String LOCAL_CONVENTION = UNCHECKED;

    /**
     * The consumer will check the value or erase to the common type.
     */
    public static final String CONSUMER_CHECKS = UNCHECKED;

    /**
     * This method exists to provide specification for implementations.
     */
    public static final String SPECIFICATION = UNUSED;
}
