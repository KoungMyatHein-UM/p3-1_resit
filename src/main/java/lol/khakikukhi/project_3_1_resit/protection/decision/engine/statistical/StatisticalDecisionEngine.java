package lol.khakikukhi.project_3_1_resit.protection.decision.engine.statistical;

import lol.khakikukhi.project_3_1_resit.protection.decision.Decision;
import lol.khakikukhi.project_3_1_resit.protection.RequestContext;
import lol.khakikukhi.project_3_1_resit.protection.ResponseContext;
import lol.khakikukhi.project_3_1_resit.protection.decision.engine.CoreDecisionEngine;
import lol.khakikukhi.project_3_1_resit.protection.decision.engine.statistical.latency.PercentileStatisticRatio;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StatisticalDecisionEngine extends CoreDecisionEngine {

    private final Map<String, PercentileStatisticRatio> endpointLatencies = new ConcurrentHashMap<>();

    @Override
    public Decision handleDecide(RequestContext context) {
        String path = context.path();
        PercentileStatisticRatio latencyStatistic = endpointLatencies.computeIfAbsent(path, k -> new PercentileStatisticRatio(1000, 200));
        return makeDecision(latencyStatistic.getRatio());
    }

    @Override
    public void feedback(RequestContext context, ResponseContext response, Decision decision) {
        String path = context.path();
        double latency = response.latencyMillis();

        PercentileStatisticRatio latencyStatistic = endpointLatencies.computeIfAbsent(path, k -> new PercentileStatisticRatio(1000, 200));
        latencyStatistic.update(latency);
    }

    private Decision makeDecision(double latencyRatio) {
        //ratio < 1.5 → ALLOW
        //ratio < 2 → DEGRADE
        //ratio ≥ 2 → REJECT

        if (latencyRatio < 1.5) {
            return Decision.ALLOW;
        } else if (latencyRatio < 2) {
            return Decision.DEGRADE;
        } else {
            return Decision.REJECT;
        }
    }

}
