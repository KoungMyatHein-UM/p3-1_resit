package lol.khakikukhi.project_3_1_resit.protection.decision.engine.statistical.latency.rolling;

public interface RollingStatistic {
    void update(double value);
    double get();
}
