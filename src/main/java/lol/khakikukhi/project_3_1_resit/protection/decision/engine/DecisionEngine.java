package lol.khakikukhi.project_3_1_resit.protection.decision.engine;

import lol.khakikukhi.project_3_1_resit.protection.RequestContext;
import lol.khakikukhi.project_3_1_resit.protection.ResponseContext;
import lol.khakikukhi.project_3_1_resit.protection.decision.Decision;

public interface DecisionEngine {
    Decision decide(RequestContext context);
    void feedback(RequestContext context, ResponseContext response, Decision decision);
//    void getStats();
}
