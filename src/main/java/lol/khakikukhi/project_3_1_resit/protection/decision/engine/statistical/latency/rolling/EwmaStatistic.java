package lol.khakikukhi.project_3_1_resit.protection.decision.engine.statistical.latency.rolling;

public class EwmaStatistic implements RollingStatistic {

    private final double alpha;
    private volatile double mean;
    private boolean initialized = false;

    public EwmaStatistic(double alpha) {
        this.alpha = alpha;
    }

    @Override
    public synchronized void update(double value) {
        if (!initialized) {
            mean = value;
            initialized = true;
        } else {
            mean = alpha * value + (1 - alpha) * mean;
        }
    }

    @Override
    public double get() {
        return mean;
    }
}
