package net.zethmayr.fungu.flock.config;

import net.zethmayr.fungu.fields.HasX;

/**
 * Has a readable initial clock value field.
 */
public interface HasInitialValue extends HasX {

    /**
     * Returns the initial clock value.
     * @return the initial value.
     */
    Long getInitialValue();
}
