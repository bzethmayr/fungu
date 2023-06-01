package net.zethmayr.fungu.flock.config;

import net.zethmayr.fungu.fields.EditsX;

/**
 * Has a mutable initial value field,
 * configuring some valid past clock value.
 */
public interface EditsInitialValue extends HasInitialValue, SetsInitialValue, EditsX {
}
