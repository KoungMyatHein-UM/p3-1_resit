package lol.khakikukhi.p31_resit.decision.engine.statistical;

import lol.khakikukhi.p31_resit.decision.Decision;
import lol.khakikukhi.p31_resit.decision.RequestContext;
import lol.khakikukhi.p31_resit.decision.ResponseContext;
import lol.khakikukhi.p31_resit.decision.engine.CoreDecisionEngine;

public class StatisticalDecisionEngine extends CoreDecisionEngine {

    @Override
    public Decision handleDecide(RequestContext context) {
        return Decision.ALLOW;
    }

    @Override
    public void feedback(RequestContext context, ResponseContext response, Decision decision) {

    }
}
