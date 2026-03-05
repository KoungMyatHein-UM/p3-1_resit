package lol.khakikukhi.project_3_1_resit.filter;

import lol.khakikukhi.project_3_1_resit.protection.admission.engine.AdmissionEngine;
import lol.khakikukhi.project_3_1_resit.protection.admission.AdmissionEntry;
import lol.khakikukhi.project_3_1_resit.protection.admission.engine.QueuedAdmissionEngine;
import lol.khakikukhi.project_3_1_resit.protection.RequestContext;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AdmissionFilter implements GlobalFilter, Ordered {

    private final AdmissionEngine admissionEngine;
    private final EndpointResolver endpointResolver;

    public AdmissionFilter(QueuedAdmissionEngine admissionEngine, EndpointResolver endpointResolver) {
        this.admissionEngine = admissionEngine;
        this.endpointResolver = endpointResolver;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        long arrivalTime = System.nanoTime();

        String resolvedPath = endpointResolver.resolve(exchange.getRequest().getPath().value());

        RequestContext requestContext = new RequestContext(
                exchange.getRequest().getMethod().name(),
                resolvedPath,
                exchange.getRequest()
                        .getRemoteAddress()
                        .getAddress()
                        .getHostAddress(),
                System.currentTimeMillis()
        );

        return Mono.create(sink -> {
            admissionEngine.submit(
                    new AdmissionEntry(
                            requestContext,
                            exchange,
                            chain,
                            sink,
                            arrivalTime
                    )
            );
        });
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
