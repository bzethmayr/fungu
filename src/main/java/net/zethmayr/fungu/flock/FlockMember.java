package net.zethmayr.fungu.flock;

import net.zethmayr.fungu.flock.config.KnownMember;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicLong;

class FlockMember {
    final AtomicLong clock;
    final URI location;

    /**
     * Returns a member with the given initial clock value and location.
     *
     * @param initialValue the initial clock value.
     * @param location     the expected network location.
     */
    public FlockMember(final long initialValue, @NotNull final URI location) {
        clock = new AtomicLong(initialValue);
        this.location = location;
    }

    public FlockMember(final KnownMember initialConfig) throws URISyntaxException {
        this(initialConfig.getInitialValue(), initialConfig.getLocationUri());
    }

    long get() {
        return clock.get();
    }

    void set(final long newValue) {
        clock.set(newValue);
    }

    long incrementAndGet() {
        return clock.incrementAndGet();
    }

    URI getLocation() {
        return location;
    }
}
