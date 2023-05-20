package net.zethmayr.fungu.flock;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static net.zethmayr.fungu.core.ExceptionFactory.becauseImpossible;

public class FlockMember {

    private volatile int memberId;
    private final AtomicReference<AtomicLong[]> clocks;

    private final Object lock = new Object();

    public FlockMember(final int memberId, final long[] clocks) {
        this.memberId = memberId;
        this.clocks = new AtomicReference<>(
                LongStream.of(clocks)
                        .mapToObj(AtomicLong::new)
                        .toArray(AtomicLong[]::new)
        );
    }

    public void locally(final Runnable event) {
        synchronized (lock) {
            event();
            event.run();
        }
    }

    public void locally(final LongConsumer sequencedEvent) {
        synchronized (lock) {
            sequencedEvent.accept(event());
        }
    }

    public void locally(final Consumer<long[]> clockedEvent) {
        synchronized (lock) {
            event();
            clockedEvent.accept(clocks());
        }
    }

    public void locally(final BiConsumer<Long, long[]> sequencedClockedEvent) {
        synchronized (lock) {
            sequencedClockedEvent.accept(event(), clocks());
        }
    }

    public long localClock() {
        synchronized (lock) {
            return clocks.get()[memberId].getAcquire();
        }
    }

    public long[] clocks() {
        return Stream.of(clocks.get())
                .mapToLong(AtomicLong::getAcquire)
                .toArray();
    }

    public void addMember(final int sequence) {
        synchronized (lock) {
            final AtomicLong[] oldClocks = clocks.get();
            final AtomicLong[] moreClocks = new AtomicLong[oldClocks.length + 1];
            System.arraycopy(oldClocks, 0, moreClocks, 0, sequence);
            moreClocks[sequence] = new AtomicLong();
            if (sequence < oldClocks.length) {
                System.arraycopy(oldClocks, sequence, moreClocks, sequence + 1, oldClocks.length - sequence);
            }
            clocks.set(moreClocks);
            if (memberId >= sequence) {
                memberId += 1;
            }
        }
    }

    public void retireMember(final int sequence) {
        synchronized (lock) {
            if (memberId == sequence) {
                throw becauseImpossible("This instance %s is already presumed dead", memberId);
            }
            final AtomicLong[] oldClocks = clocks.get();
            final AtomicLong[] lessClocks = new AtomicLong[oldClocks.length - 1];
            System.arraycopy(oldClocks, 0, lessClocks, 0, sequence);
            if (sequence < lessClocks.length) {
                System.arraycopy(oldClocks, sequence + 1, lessClocks, sequence, lessClocks.length - sequence);
            }
            clocks.set(lessClocks);
            if (memberId > sequence) {
                memberId -= 1;
            }
        }
    }

    public long event() {
        synchronized (lock) {
            return clocks.get()[memberId].incrementAndGet();
        }
    }
}
