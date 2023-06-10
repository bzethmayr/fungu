package net.zethmayr.fungu.flock;

import net.zethmayr.fungu.flock.config.HasKnownMembers;

import java.io.IOException;

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
     *
     * @param knownMembers minimal preexisting configuration.
     * @throws IOException if network transport fails.
     * @throws NopException if the remote rejected the addition.
     */
    void joinFlock(final HasKnownMembers knownMembers) throws IOException, NopException;

    /**
     * Re-joins the flock described by the given configuration, as an existing member.
     * After this operation succeeds, the local clocks will represent
     * some recent current minimums from a known remote location.
     *
     * @param memberId     last known member ID.
     * @param knownMembers last known configuration.
     * @throws IOException if network transport fails.
     * @throws NopException if the remote rejected the re-join.
     */
    void reJoinFlock(final int memberId, final HasKnownMembers knownMembers) throws IOException, NopException;

    /**
     * Accepts a remote message and associated clock values.
     *
     * @param message    the message.
     * @param sentClocks the message's unique clock values.
     * @throws NopException if the message was rejected.
     */
    void receiveMessage(final T message, final long[] sentClocks) throws NopException;

    /**
     * Sends a message.
     *
     * @param message the message.
     * @throws IOException if network transport fails.
     * @throws RetryableNopException if the message was rejected.
     */
    void sendMessage(final T message) throws IOException, RetryableNopException;
}
