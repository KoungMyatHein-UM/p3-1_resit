package lol.khakikukhi.project_3_1_resit.protection.decision.engine.statistical;

import lol.khakikukhi.project_3_1_resit.protection.decision.Decision;
import lol.khakikukhi.project_3_1_resit.protection.RequestContext;
import lol.khakikukhi.project_3_1_resit.protection.ResponseContext;
import lol.khakikukhi.project_3_1_resit.protection.decision.engine.CoreDecisionEngine;
import org.springframework.stereotype.Component;

@Component
public class StatisticalDecisionEngine extends CoreDecisionEngine {

    @Override
    public Decision handleDecide(RequestContext context) {
        return Decision.ALLOW;
    }

    @Override
    public void feedback(RequestContext context, ResponseContext response, Decision decision) {

    }
}
