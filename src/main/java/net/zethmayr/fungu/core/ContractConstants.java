package net.zethmayr.fungu.core;

import static net.zethmayr.fungu.core.ExceptionFactory.becauseConstantsOnly;

public final class ContractConstants {

    private ContractConstants() {
        throw becauseConstantsOnly();
    }

    public static final String IDENTITY = "_->param1";
}
