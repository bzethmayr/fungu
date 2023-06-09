package net.zethmayr.fungu.core;

import static net.zethmayr.fungu.core.ExceptionFactory.becauseConstantsOnly;

/**
 * Declares constants used with the {@link org.jetbrains.annotations.Contract} annotation.
 */
public final class ContractConstants {

    private ContractConstants() {
        throw becauseConstantsOnly();
    }

    /**
     * Indicates that this unary method always returns the argument passed, {@literal i.e.} is an identity method.
     */
    public static final String IDENTITY = "_->param1";
}
