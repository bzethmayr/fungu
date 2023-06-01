package net.zethmayr.fungu.capabilities;

import net.zethmayr.fungu.fields.HasX;

/**
 * Has a readable index field.
 */
public interface HasIndex extends HasX {

    /**
     * Returns the index field value, if any.
     * @return the index value.
     */
    Integer getIndex();
}
