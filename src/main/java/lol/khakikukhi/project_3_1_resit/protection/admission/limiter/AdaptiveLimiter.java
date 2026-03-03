package lol.khakikukhi.project_3_1_resit.protection.admission.limiter;


import static java.lang.Math.max;

public class AdaptiveLimiter implements Limiter {

    private final int minLimit;
    private final int maxLimit;
    private int currentLimit;

    private long degradeCount = 0;
    private long acceptCount = 0;

    public AdaptiveLimiter(int minLimit, int maxLimit, int currentLimit) {
        this.minLimit = minLimit;
        this.maxLimit = maxLimit;
        this.currentLimit = currentLimit;
    }

    public AdaptiveLimiter() {
        this(1, 100, 100);
    }

    @Override
    public synchronized int getLimitAndReset() {
        currentLimit = updateLimit();
        reset();
        return currentLimit;
    }

    @Override
    public synchronized void  incrementAccepted() {
        acceptCount++;
    }

    @Override
    public synchronized void incrementDegraded() {
        degradeCount++;
    }

    private int updateLimit() {
        long total = acceptCount + degradeCount;
        float degradeRatio = total == 0 ? 0 : (float) degradeCount / total;

        if (total == 0 || degradeRatio < 0.3f) {
            return Math.min(maxLimit, currentLimit + 1);
        } else if  (degradeRatio > 0.6f) {
            return max(minLimit, currentLimit / 2);
        }

        return currentLimit;

    }

    private void reset() {
        degradeCount = 0;
        acceptCount = 0;
    }
}
