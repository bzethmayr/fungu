package net.zethmayr.fungu.flock;

import net.zethmayr.fungu.capabilities.SingleUse;

/**
 * This checked exception indicates that the operation has no-oped and can be retried,
 * and has a very low cost to throw at the cost of a usable stack trace.
 */
@SingleUse
public class IgnorableNopException extends Exception {

    IgnorableNopException(final String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    static IgnorableNopException becauseNotYetPossible(final String message) {
        return new IgnorableNopException(message);
    }
}
