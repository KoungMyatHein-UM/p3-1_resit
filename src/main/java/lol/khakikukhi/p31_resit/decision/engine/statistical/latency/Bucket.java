package lol.khakikukhi.p31_resit.decision.engine.statistical.latency;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.atomic.LongAdder;

@Getter
@RequiredArgsConstructor
public final class Bucket {
    private volatile long timestamp;
    private final LongAdder count = new LongAdder();
    private final DoubleAdder sumLatency = new DoubleAdder();

    void reset(long newTimestamp) {
        timestamp = newTimestamp;
        count.reset();
        sumLatency.reset();
    }

    void add(double latency) {
        count.increment();
        sumLatency.add(latency);
    }

}
