package org.linn.filter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * gateway局部过滤器，在请求头部携带token并对其进行验证
 * <li>创建一个类，实现GatewayFilter 接口</li>
 * <li>在factor中加入过滤器</li>
 */
public class HeaderTokenGatewayFilter implements GatewayFilter, Ordered {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		// 从http Header中寻找key 为token
		String name = exchange.getRequest().getHeaders().getFirst("token");
		if (StringUtils.equals("linn", name)) {
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
