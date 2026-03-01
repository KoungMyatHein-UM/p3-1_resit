package lol.khakikukhi.p31_resit.engines.decision;

public record RequestContext(String method, String path, String clientId, long timestamp) {
}
