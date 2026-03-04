package lol.khakikukhi.project_3_1_resit.protection.decision.engine.statistical;

import lol.khakikukhi.project_3_1_resit.protection.decision.Decision;
import lol.khakikukhi.project_3_1_resit.protection.RequestContext;
import lol.khakikukhi.project_3_1_resit.protection.ResponseContext;
import lol.khakikukhi.project_3_1_resit.protection.decision.engine.CoreDecisionEngine;
import lol.khakikukhi.project_3_1_resit.protection.decision.engine.statistical.latency.rolling.WindowMeanStatistic;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StatisticalDecisionEngine extends CoreDecisionEngine {

    private final Map<String, WindowMeanStatistic> endpointLatencies = new ConcurrentHashMap<>();

    @Override
    public Decision handleDecide(RequestContext context) {
        return Decision.ALLOW;
    }

    @Override
    public void feedback(RequestContext context, ResponseContext response, Decision decision) {

    }
}
