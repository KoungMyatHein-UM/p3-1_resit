package lol.khakikukhi.p31_resit.decision.engine.statistical.latency;

public final  class LatencyTimeWindow {
    private final Bucket[] buckets;
    private final long bucketDurationMillis;
    private final int bucketCount;
    private final long windowSizeMillis;

    public LatencyTimeWindow(int bucketCount, long bucketDurationMillis) {
        this.bucketCount = bucketCount;
        this.bucketDurationMillis = bucketDurationMillis;
        this.buckets = new Bucket[bucketCount];
        this.windowSizeMillis = bucketCount * bucketDurationMillis;

        for (int i = 0; i < bucketCount; i++) {
            buckets[i] = new Bucket();
        }
    }

    public void record(double latency, long nowMillis) {
        long bucketTime = nowMillis / bucketDurationMillis;
        int index = (int) (bucketTime % bucketCount);

        Bucket bucket = buckets[index];

        long bucketStartTime = bucketTime * bucketDurationMillis;

        if (bucket.getTimestamp() != bucketStartTime) {
            synchronized (bucket) {
                if (bucket.getTimestamp() != bucketStartTime) {
                    bucket.reset(bucketStartTime);
                }
            }
        }

        bucket.add(latency);
    }

    public double mean(long nowMillis) {
        long currentTime = nowMillis;
        long minValidTime = currentTime - (windowSizeMillis);

        double sum = 0;
        long count = 0;

        for (Bucket bucket : buckets) {
            if (bucket.getTimestamp() >= minValidTime) {
                sum += bucket.getSumLatency().sum();
                count += bucket.getCount().sum();
            }
        }

        return count == 0 ? 0.0 : sum / count;
    }

}
