package lol.khakikukhi.project_3_1_resit.protection.admission.limiter;

public interface Limiter {
    int getLimitAndReset();
    void incrementAccepted();
    void incrementDegraded();
}
