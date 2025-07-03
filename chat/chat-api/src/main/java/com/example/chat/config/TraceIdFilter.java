package com.example.chat.config;

import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.List;
import java.util.UUID;

@Component
public class TraceIdFilter implements WebFilter {

    /*
     * TODO Micrometer Tracing 사용
     */
    private static final String TRACE_ID_HEADER = "X-Trace-Id";
    private static final String TRACE_ID_KEY = "traceId";

    @Override
    public @NonNull Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        // 요청 헤더에 traceId가 있으면 사용하고, 없으면 새로 생성
        String traceId = exchange.getRequest().getHeaders()
                .getOrDefault(TRACE_ID_HEADER, List.of(UUID.randomUUID().toString()))
                .get(0);

        MDC.put(TRACE_ID_KEY, traceId);

        return chain.filter(exchange)
                .contextWrite(Context.of(TRACE_ID_HEADER, traceId))
                .doFinally(s -> MDC.remove(TRACE_ID_KEY));
    }
}
