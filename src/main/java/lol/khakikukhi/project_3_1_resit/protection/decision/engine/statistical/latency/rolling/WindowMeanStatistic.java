package lol.khakikukhi.project_3_1_resit.protection.decision.engine.statistical.latency.rolling;

public class WindowMeanStatistic implements RollingStatistic {
    private final double[] values;
    private int index = 0;
    private int count = 0;
    private double sum = 0;

    public WindowMeanStatistic(int size) {
        this.values = new double[size];
    }

    @Override
    public synchronized void update(double value) {
        int pos = index % values.length;

        if (count == values.length) {
            sum -= values[pos];
        } else {
            count++;
        }

        values[pos] = value;
        sum += value;

        index++;
    }

    @Override
    public synchronized double get() {
        return count == 0 ? 0 : sum / count;
    }
}
