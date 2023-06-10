package net.zethmayr.fungu.flock;

/**
 * Indicates that an operation has been rejected.
 */
public class NopException extends Exception {

    NopException(final String message) {
        super(message);
    }
}
