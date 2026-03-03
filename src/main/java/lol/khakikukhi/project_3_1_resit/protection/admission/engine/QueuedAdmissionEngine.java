package lol.khakikukhi.project_3_1_resit.protection.admission.engine;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lol.khakikukhi.project_3_1_resit.protection.admission.AdmissionEntry;
import lol.khakikukhi.project_3_1_resit.protection.admission.limiter.AdaptiveLimiter;
import lol.khakikukhi.project_3_1_resit.protection.admission.limiter.Limiter;
import lol.khakikukhi.project_3_1_resit.protection.decision.engine.DecisionEngine;
import lol.khakikukhi.project_3_1_resit.protection.ResponseContext;
import lol.khakikukhi.project_3_1_resit.protection.decision.Decision;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.*;

@Component
public class QueuedAdmissionEngine implements AdmissionEngine {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private final DecisionEngine decisionEngine;

    private final Map<String, Queue<AdmissionEntry>> queueMap = new ConcurrentHashMap<>();
    private final Map<String, Limiter> limiterMap = new ConcurrentHashMap<>();

    private final int ttlMillis = 2000;
    private final int tickIntervalMillis = 100;

    public QueuedAdmissionEngine(DecisionEngine decisionEngine) {
        this.decisionEngine = decisionEngine;
    }

    @Override
    public void submit(AdmissionEntry entry) {
        Decision decision = decisionEngine.decide(entry.getContext());
        decision = Decision.DEGRADE; // FOR TESTING ONLY

        entry.setDecision(decision);

        switch (decision) {
            case ALLOW -> {
                updateLimiter(entry);
                processEntry(entry);
            }
            case DEGRADE -> {
                updateLimiter(entry);
                addToQueue(entry);
            }
            case REJECT -> processEntry(entry);
        }
    }

    private void addToQueue(AdmissionEntry entry) {
        String path = entry.getContext().path();

        Queue<AdmissionEntry> queue = this.queueMap.computeIfAbsent(path, k -> new ConcurrentLinkedQueue<>());

        queue.add(entry);
    }

    private void updateLimiter(AdmissionEntry entry) {
        Limiter limiter = this.limiterMap.computeIfAbsent(entry.getContext().path(), k -> new AdaptiveLimiter());

        Decision decision = entry.getDecision();
        switch (decision) {
            case ALLOW -> limiter.incrementAccepted();
            case DEGRADE -> limiter.incrementDegraded();
        }
    }

    private void doFeedback(AdmissionEntry entry, ResponseContext response) {
        decisionEngine.feedback(entry.getContext(), response, entry.getDecision());
    }

    private void processQueues() {
        for (String paths : queueMap.keySet()) {
            Queue<AdmissionEntry> queue = queueMap.get(paths);
            Limiter limiter = limiterMap.computeIfAbsent(paths, k -> new AdaptiveLimiter());

            int permits = limiter.getLimitAndReset();

            while (permits-- > 0) {
                AdmissionEntry entry = queue.poll();
                if (entry == null) {
                    break;
                }
                System.out.println("Processing entry for " + entry.getContext().path() + ": " + entry);
                processEntry(entry);
            }
        }

    }

    private void processEntry(AdmissionEntry entry) {
        long ageMillis = (System.nanoTime() - entry.getArrivalTime()) / 1_000_000;

        if (ageMillis > ttlMillis) {
            ResponseContext response = new ResponseContext(503, 0);
            doFeedback(entry, response);
            entry.getSink().error(new RuntimeException("Request expired"));
            return;
        } else if (entry.getDecision() == Decision.REJECT) {
            ResponseContext response = new ResponseContext(403, 0);
            doFeedback(entry, response);
            entry.getSink().error(new RuntimeException("Request rejected"));
            return;
        }

        long start = System.nanoTime();

        entry.getChain()
                .filter(entry.getExchange())
                .doOnSuccess(v -> {
                    long latencyMillis =
                            (System.nanoTime() - start) / 1_000_000;

                    var statusObj = entry.getExchange().getResponse().getStatusCode();
                    int status = statusObj != null ? statusObj.value() : 200;

                    ResponseContext response = new ResponseContext(status, latencyMillis);

                    doFeedback(entry, response);

                    entry.getSink().success();
                })
                .doOnError(error -> {
                    ResponseContext response = new ResponseContext(500, 0);
                    doFeedback(entry, response);
                    entry.getSink().error(error);
                })
                .subscribe();
    }

    @PostConstruct
    public void start() {
        scheduler.scheduleAtFixedRate(this::processQueues, 0, tickIntervalMillis, TimeUnit.MILLISECONDS);
    }

    @PreDestroy
    public void shutdown() {
        scheduler.shutdown();
    }

}
