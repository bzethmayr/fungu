package net.zethmayr.fungu.flock.config;

import net.zethmayr.fungu.capabilities.HasIndex;
import net.zethmayr.fungu.capabilities.HasLocation;

/**
 * Readable configuration for a flock member,
 * composing {@link HasLocation location},
 * {@link HasIndex index}, and
 * {@link HasInitialValue} fields.
 */
public interface KnownMember extends HasLocation, HasIndex, HasInitialValue {

}
