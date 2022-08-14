package org.linn.config;

import org.linn.constant.GatewayConstant;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置登陆请求转发规则
 */
@Configuration
public class RouteLocatorConfig {

	/**
	 * 定义路由规则，在网关拦截登陆注册
	 */
	@Bean
	public RouteLocator routeLocator(RouteLocatorBuilder builder) {
		// 手动定义Gateway，路由规则需要指定 id path uri
		return builder.routes()
				   .route("ec-oms-auth",
					   r -> r.path(GatewayConstant.LOGIN_URL, GatewayConstant.REGISTER_URL)
								.uri("http://localhost:9000")
				   )
				   .build();
	}
}
