package lol.khakikukhi.project_3_1_resit.protection;

public record RequestContext(String method, String path, String clientId, long timestamp) {
}
