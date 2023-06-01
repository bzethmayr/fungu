package net.zethmayr.fungu.flock;

import net.zethmayr.fungu.flock.config.HasKnownMembers;

/**
 * Lifecycle operations for the local flock member.
 *
 * @param <T> the common message type.
 */
public interface FlockProtocol<T> {

    /**
     * Joins the flock described by the given configuration, as a new member.
     * After this operation succeeds, the local clocks will represent
     * some recent current minimums from a known remote location.
     * @param knownMembers minimal preexisting configuration.
     */
    void joinFlock(final HasKnownMembers knownMembers);

    /**
     * Re-joins the flock described by the given configuration, as an existing member.
     * After this operation succeeds, the local clocks will represent
     * some recent current minimums from a known remote location.
     * @param memberId last known member ID.
     * @param knownMembers last known configuration.
     */
    void reJoinFlock(final int memberId, final HasKnownMembers knownMembers);

    /**
     * Accepts a remote message and associated clock values.
     * @param message the message.
     * @param sentClocks the message's unique clock values.
     */
    void receiveMessage(final T message, final long[] sentClocks);

    /**
     * Sends a message.
     * @param message the message.
     */
    void sendMessage(final T message);
}
