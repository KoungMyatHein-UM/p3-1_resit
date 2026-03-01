package lol.khakikukhi.p31_resit.engines.decision.engine.statistical;

import lol.khakikukhi.p31_resit.engines.decision.engine.Decision;
import lol.khakikukhi.p31_resit.engines.decision.RequestContext;
import lol.khakikukhi.p31_resit.engines.decision.ResponseContext;
import lol.khakikukhi.p31_resit.engines.decision.engine.CoreDecisionEngine;

public class StatisticalDecisionEngine extends CoreDecisionEngine {

    @Override
    public Decision handleDecide(RequestContext context) {
        return Decision.ALLOW;
    }

    @Override
    public void feedback(RequestContext context, ResponseContext response, Decision decision) {

    }
}
