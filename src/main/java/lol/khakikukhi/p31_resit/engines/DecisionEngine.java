package lol.khakikukhi.p31_resit.engines;

import lol.khakikukhi.p31_resit.engines.decision.RequestContext;
import lol.khakikukhi.p31_resit.engines.decision.ResponseContext;
import lol.khakikukhi.p31_resit.engines.decision.engine.Decision;

public interface DecisionEngine {
    Decision decide(RequestContext context);
    void feedback(RequestContext context, ResponseContext response, Decision decision);
//    void getStats();
}
