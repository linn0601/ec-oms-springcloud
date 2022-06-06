package org.linn.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 缓存请求body的全局过滤器
 * spring webflux
 */
@Component
public class GlobalCacheRequestBodyFilter implements GlobalFilter, Ordered {

	private static final Logger logger = LoggerFactory.getLogger(GlobalCacheRequestBodyFilter.class);

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		// String path = exchange.getRequest().getURI().getPath().contains();

		return null;
	}

	@Override
	public int getOrder() {
		return 0;
	}
}
