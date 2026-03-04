package lol.khakikukhi.project_3_1_resit.protection.decision.engine.statistical.latency.percentile;

import lol.khakikukhi.project_3_1_resit.protection.decision.engine.statistical.latency.rolling.RollingStatistic;

import java.util.Arrays;

public class PercentileStatistic implements RollingStatistic {

    private final double[] values;
    private int index = 0;
    private int count = 0;
    private final double percentile;
    private final int minSamples = 50;

    public PercentileStatistic(int windowSize, double percentile) {
        this.values = new double[windowSize];
        this.percentile = percentile;
    }

    @Override
    public synchronized void update(double value) {
        values[index] = value;
        index = (index + 1) % values.length;

        if (count < values.length) {
            count++;
        }
    }

    @Override
    public synchronized double get() {
        if (count == 0) {
            return 0;
        }
        else if (count < minSamples) {
            return 0;
        }

        double[] copy = Arrays.copyOf(values, count);

        Arrays.sort(copy);

        int percentileIndex  = (int) Math.ceil(this.percentile * count) - 1;
        return copy[Math.max(0, percentileIndex )];
    }
}
