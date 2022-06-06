package org.linn.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * gateway局部过滤器，在请求头部携带token并对其进行验证
 */
public class HeadTokenGatewayFilter implements GatewayFilter, Ordered {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		// 从http Header中寻找key 为token
		String name = exchange.getRequest().getHeaders().getFirst("ec-oms-token");
		if ("".equals(name)) {
			return chain.filter(exchange);
		}
		// 标记无权限
		exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
		return exchange.getResponse().setComplete();
	}

	@Override
	public int getOrder() {
		return HIGHEST_PRECEDENCE + 2;
	}
}
