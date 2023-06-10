package net.zethmayr.fungu.flock;

import net.zethmayr.fungu.flock.config.KnownMember;

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
    public FlockMember(final long initialValue, final URI location) {
        clock = new AtomicLong(initialValue);
        this.location = location;
    }

    /**
     * Returns a local, solely index-addressable member without a network location.
     *
     * @param initialValue the initial count value.
     */
    public FlockMember(final long initialValue) {
        this(initialValue, null);
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
