package net.zethmayr.fungu.flock.config;

import net.zethmayr.fungu.capabilities.EditsIndex;
import net.zethmayr.fungu.capabilities.EditsLocation;

/**
 * Configuration for a flock member -
 * composes {@link EditsIndex index},
 * {@link EditsInitialValue initial value}, and
 * {@link EditsLocation location} fields
 */
public interface ConfiguresKnownMember extends KnownMember, EditsIndex, EditsLocation, EditsInitialValue {
}
