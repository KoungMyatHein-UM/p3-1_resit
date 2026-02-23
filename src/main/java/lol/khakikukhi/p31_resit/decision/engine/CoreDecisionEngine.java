package lol.khakikukhi.p31_resit.decision.engine;

import lol.khakikukhi.p31_resit.decision.Decision;
import lol.khakikukhi.p31_resit.decision.RequestContext;
import lol.khakikukhi.p31_resit.decision.ResponseContext;

import java.util.concurrent.atomic.AtomicLong;

public abstract class CoreDecisionEngine implements DecisionEngine {
    private AtomicLong totalRejected = new AtomicLong(0);
    private AtomicLong totalDegraded = new AtomicLong(0);
    private AtomicLong totalAllowed = new AtomicLong(0);
    private AtomicLong totalRequests = new AtomicLong(0);

    public abstract Decision handleDecide(RequestContext context);

    @Override
    public abstract void feedback(RequestContext context, ResponseContext response, Decision decision);

    @Override
    public Decision decide(RequestContext context) {
        incrementRequests();

        Decision decision = handleDecide(context);

        updateDecisionCounters(decision);

        return decision;

    }

    private void updateDecisionCounters(Decision decision) {
        switch (decision) {
            case ALLOW -> incrementAllowed();
            case REJECT -> incrementRejected();
            case DEGRADE -> incrementDegraded();
        }
    }


    public double getRejectRate() {
        return safeDivide(totalRejected.get(), totalRequests.get());
    }

    public double getDegradeRate() {
        return safeDivide(totalDegraded.get(), totalRequests.get());
    }

    public double getAllowedRate() {
        return safeDivide(totalAllowed.get(), totalRequests.get());
    }

    private void incrementRequests() {
        totalRequests.incrementAndGet();
    }

    private void incrementRejected() {
        totalRejected.incrementAndGet();
    }

    private void incrementDegraded() {
        totalDegraded.incrementAndGet();
    }

    private void incrementAllowed() {
        totalAllowed.incrementAndGet();
    }

    private double safeDivide(double a, double b) {
        return b == 0 ? 0 : a / b;
    }

    public long getTotalRejected() {
        return this.totalRejected.get();
    }

    public long getTotalDegraded() {
        return this.totalDegraded.get();
    }

    public long getTotalAllowed() {
        return this.totalAllowed.get();
    }

    public long getTotalRequests() {
        return this.totalRequests.get();
    }
}
