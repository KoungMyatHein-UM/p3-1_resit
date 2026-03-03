package lol.khakikukhi.project_3_1_resit.protection.admission;

import lol.khakikukhi.project_3_1_resit.protection.RequestContext;
import lol.khakikukhi.project_3_1_resit.protection.decision.Decision;
import lombok.Data;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.MonoSink;

@Data
public class AdmissionEntry {
    private final RequestContext context;
    private final ServerWebExchange exchange;
    private final GatewayFilterChain chain;
    private final MonoSink<Void> sink;
    private final long arrivalTime;
    private Decision decision = Decision.PENDING;
}
