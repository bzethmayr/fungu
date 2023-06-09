package net.zethmayr.fungu.core;

import static net.zethmayr.fungu.core.SuppressionConstants.LiteralValues.UNCHECKED;

/**
 * Defines constants used to document uses of {@link SuppressWarnings}.
 */
public class SuppressionConstants {

    private SuppressionConstants() {
        throw ExceptionFactory.becauseConstantsOnly();
    }

    static final class LiteralValues {

        private LiteralValues() {
            throw ExceptionFactory.becauseConstantsOnly();
        }

        static final String UNCHECKED = "unchecked";
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
}
