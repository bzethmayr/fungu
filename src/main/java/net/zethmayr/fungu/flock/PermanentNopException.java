package net.zethmayr.fungu.flock;

import java.util.function.Supplier;

/**
 * Thrown to indicate that an operation has been rejected
 * permanently and should not be retried.
 */
public class PermanentNopException extends NopException {

    /**
     * Creates a new instance
     * with the given permanent rejection reason.
     *
     * @param message the rejection reason.
     */
    public PermanentNopException(final String message) {
        super(message);
    }

    /**
     * Returns a new instance
     * with the given permanent rejection reason.
     *
     * @param message the rejection reason.
     * @return a new exception.
     */
    public static PermanentNopException becausePermanentNop(final String message) {
        return new PermanentNopException(message);
    }

    /**
     * Returns an exception supplier
     * with the given permanent rejection reason.
     *
     * @param message the rejection reason.
     * @return an exception supplier.
     */
    public static Supplier<PermanentNopException> permanentNopBecause(final String message) {
        return () -> becausePermanentNop(message);
    }
}
