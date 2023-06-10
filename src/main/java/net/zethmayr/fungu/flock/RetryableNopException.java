package net.zethmayr.fungu.flock;

import java.util.function.Supplier;

/**
 * Thrown to indicate that an operation has been rejected
 * and may be retried.
 */
public class RetryableNopException extends NopException {

    /**
     * Creates a new instance
     * with the given retryable rejection reason.
     * @param message the rejection reason.
     */
    public RetryableNopException(final String message) {
        super(message);
    }

    /**
     * Returns a new instance
     * with the given retryable rejection reason.
     * @param message the rejection reason.
     * @return a new exception.
     */
    public static RetryableNopException becauseRetryNop(final String message) {
        return new RetryableNopException(message);
    }

    /**
     * Returns an exception supplier
     * with the given retryable rejection reason.
     * @param message the rejection reason.
     * @return an exception supplier.
     */
    public static Supplier<RetryableNopException> retryNopBecause(final String message) {
        return () -> becauseRetryNop(message);
    }
}
