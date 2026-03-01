package lol.khakikukhi.p31_resit.engines.decision.engine.statistical.latency;

import lombok.Getter;

public class RollingContainer {
    private final double[] values;
    private int index = 0;
    private int count;
    private double sum;

    @Getter
    private volatile double mean;

    public RollingContainer(int size) {
        this.values = new double[size];
    }

    public synchronized void addValue(double value) {
        int pos = index % values.length;

        if (count == values.length) {
            sum -= values[pos]; // remove overwritten value
        } else {
            count++;
        }

        values[pos] = value;
        sum += value;

        index++;
        mean = sum / count;
    }

}
