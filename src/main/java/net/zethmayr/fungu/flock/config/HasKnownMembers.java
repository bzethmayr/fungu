package net.zethmayr.fungu.flock.config;

import net.zethmayr.fungu.fields.HasX;
import net.zethmayr.fungu.flock.config.KnownMember;

import java.util.List;

/**
 * Has a readable field with a list of known member configurations.
 */
public interface HasKnownMembers extends HasX {

    /**
     * Returns a list of known members.
     * @return a list of known members.
     */
    List<KnownMember> getKnownMembers();
}
