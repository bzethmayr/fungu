package net.zethmayr.fungu.flock.config;

import net.zethmayr.fungu.fields.SetsX;

/**
 * Can accept an initial clock value.
 */
public interface SetsInitialValue extends SetsX {

    /**
     * Sets a new initial clock value.
     * @param initialValue an initial clock value.
     */
    void setInitialValue(final Long initialValue);
}
