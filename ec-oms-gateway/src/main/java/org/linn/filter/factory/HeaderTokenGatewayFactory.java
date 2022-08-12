package org.linn.filter.factory;

import org.linn.filter.HeaderTokenGatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

/**
 * 局部过滤器注册
 */
@Component
public class HeaderTokenGatewayFactory extends AbstractGatewayFilterFactory<Object> {

	@Override
	public GatewayFilter apply(Object config) {
		return new HeaderTokenGatewayFilter();
	}
}
