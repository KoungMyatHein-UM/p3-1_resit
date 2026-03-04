package lol.khakikukhi.project_3_1_resit.protection.decision.engine.statistical.latency.percentile;

public class PercentilePair {
    private final PercentileStatistic p50Statistic;
    private final PercentileStatistic p95Statistic;

    public PercentilePair(int windowSize50, int windowSize95) {
        p50Statistic = new PercentileStatistic(windowSize50, 0.5);
        p95Statistic = new PercentileStatistic(windowSize95, 0.95);
    }

    double getRatio() {
        double p50 = p50Statistic.get();
        double p95 = p95Statistic.get();

        if (p50 == 0) {
            return 1;
        }

        return p95 / p50;
    }

    void update(double value) {
        p50Statistic.update(value);
        p95Statistic.update(value);


    }

}
