package org.linn.filter;

import org.linn.constant.GatewayConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 缓存请求body的全局过滤器 spring webflux
 */
@Component
@SuppressWarnings("all")
public class GlobalCacheRequestBodyFilter implements GlobalFilter, Ordered {

	static {
		System.out.println("最先被执行的全局过滤器");
	}

	private static final Logger logger = LoggerFactory.getLogger(GlobalCacheRequestBodyFilter.class);

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

		boolean isLoginOrRegister =
			exchange.getRequest().getURI().getPath().contains(GatewayConstant.LOGIN_URL)
				|| exchange.getRequest().getURI().getPath().contains(GatewayConstant.REGISTER_URL);

		if (null == exchange.getRequest().getHeaders().getContentType() || !isLoginOrRegister) {
			return chain.filter(exchange);
		}

		// dataBuffer.join拿到请求中数据，
		return DataBufferUtils.join(exchange.getRequest().getBody())
				   .flatMap(dataBuffer -> {
					   // 确保数据缓冲区不被释放
					   DataBufferUtils.retain(dataBuffer);
					   // 创建数据源
					   Flux<DataBuffer> cachedFlux = Flux.defer(
						   () -> Flux.just(dataBuffer.slice(0, dataBuffer.readableByteCount())));
					   // 重新包装创建http
					   ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
						   @Override
						   public Flux<DataBuffer> getBody() {
							   return cachedFlux;
						   }
					   };
					   // 将包装之后httpServlet向下继续传递
					   return chain.filter(exchange.mutate().request(mutatedRequest).build());
				   });
	}

	@Override
	public int getOrder() {
		return HIGHEST_PRECEDENCE + 1;
	}
}
