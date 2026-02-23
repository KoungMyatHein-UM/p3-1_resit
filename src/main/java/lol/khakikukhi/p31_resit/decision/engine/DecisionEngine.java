package lol.khakikukhi.p31_resit.decision.engine;

import lol.khakikukhi.p31_resit.decision.Decision;
import lol.khakikukhi.p31_resit.decision.RequestContext;
import lol.khakikukhi.p31_resit.decision.ResponseContext;

public interface DecisionEngine {
    Decision decide(RequestContext context);
    void feedback(RequestContext context, ResponseContext response, Decision decision);
//    void getStats();
}
