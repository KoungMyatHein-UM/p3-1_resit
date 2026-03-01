package lol.khakikukhi.p31_resit.filter;

import lol.khakikukhi.p31_resit.engines.decision.RequestContext;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AdmissionFilter implements GlobalFilter, Ordered {

    private final AdmissionEngine admissionEngine;

    public AdmissionFilter(AdmissionEngine admissionEngine) {
        this.admissionEngine = admissionEngine;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {

        long arrivalTime = System.nanoTime();

        RequestContext ctx = new RequestContext(
                exchange.getRequest().getMethodValue(),
                exchange.getRequest().getPath().value(),
                exchange.getRequest()
                        .getRemoteAddress()
                        .getAddress()
                        .getHostAddress(),
                System.currentTimeMillis()
        );

        return Mono.create(sink -> {
            admissionEngine.submit(
                    ctx,
                    exchange,
                    chain,
                    sink,
                    arrivalTime
            );
        });
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
