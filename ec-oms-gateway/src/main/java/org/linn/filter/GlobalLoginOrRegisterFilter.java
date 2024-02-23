package org.linn.filter;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.commons.lang3.StringUtils;
import org.linn.constant.GatewayConstant;
import org.linn.exception.GatewayException;
import org.linn.rsa.PublicKeyConstant;
import org.linn.util.GsonUtils;
import org.linn.util.TokenParseUtil;
import org.linn.vo.JwtToken;
import org.linn.vo.UserInfo;
import org.linn.vo.UsernamePassword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class GlobalLoginOrRegisterFilter implements GlobalFilter, Ordered {

	private static final Logger logger = LoggerFactory.getLogger(GlobalLoginOrRegisterFilter.class);

	/**
	 * 注册中新客户端，可以从注册中心获取实例信息
	 */
	private final LoadBalancerClient loadBalancerClient;

	private final RestTemplate restTemplate;

	public GlobalLoginOrRegisterFilter(LoadBalancerClient loadBalancerClient,
									   RestTemplate restTemplate) {
		this.loadBalancerClient = loadBalancerClient;
		this.restTemplate = restTemplate;
	}

	/**
	 * 登陆 注册 鉴权
	 * <li>如果是登陆或这册，择取授权中心拿到 Token 并返回给客户端</li>
	 * <li>如果是访问其它的服务，则鉴权，没有返回401</li>
	 */
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

		ServerHttpRequest request = exchange.getRequest();
		ServerHttpResponse response = exchange.getResponse();

		// 如果是登陆
		if (request.getURI().getPath().contains(GatewayConstant.LOGIN_URL)) {
			// 去授权中心获取token
			String token = getTokenFromAuthorityCenter(
				request, GatewayConstant.AUTHORITY_CENTER_TOKEN_URL_FORMAT
			);

			// 给响应头加上token
			response.getHeaders().add(
				PublicKeyConstant.JWT_USER_INFO_KEY, StringUtils.isBlank(token) ? "null" : token
			);

			response.setStatusCode(HttpStatus.OK);
			return response.setComplete();
		}

		// 如果是注册
		if (request.getURI().getPath().contains(GatewayConstant.REGISTER_URL)) {
			String token = getTokenFromAuthorityCenter(request, GatewayConstant.AUTHORITY_CENTER_REGISTER_URL_FORMAT);

			// 给响应头加上token
			response.getHeaders().add(
				PublicKeyConstant.JWT_USER_INFO_KEY, StringUtils.isBlank(token) ? "null" : token
			);

			response.setStatusCode(HttpStatus.OK);
			return response.setComplete();
		}

		// 访问其他服务
		HttpHeaders headers = request.getHeaders();
		String token = headers.getFirst(PublicKeyConstant.JWT_USER_INFO_KEY);
		UserInfo userInfo = null;
		try {
			userInfo = TokenParseUtil.parseUserInfoToken(token);
		}
		catch (Exception e) {
			logger.error("parse user info from token error: [{}] ", e.getMessage(), e);
		}

		if (Objects.isNull(userInfo)) {
			response.setStatusCode(HttpStatus.UNAUTHORIZED);
			return response.setComplete();
		}
		else {
			return chain.filter(exchange);
		}
	}

	@Override
	public int getOrder() {
		return HIGHEST_PRECEDENCE + 2;
	}

	/**
	 * 从授权中心获取token
	 */
	private String getTokenFromAuthorityCenter(ServerHttpRequest request, String uriFormat) {
		// 从nacos中获取指定的服务实例
		ServiceInstance serviceInstance = loadBalancerClient.choose(
			PublicKeyConstant.AUTHORITY_CENTER_SERVICE_ID
		);

		if (null == serviceInstance) {
			throw new GatewayException("从nacos获取服务失败，请检查服务管理");
		}

		if (logger.isDebugEnabled()) {
			logger.info("Nacos Client Info: [{}], [{}], [{}]",
				serviceInstance.getServiceId(), serviceInstance.getInstanceId(),
				GsonUtils.getGson().toJson(serviceInstance.getMetadata()));
		}

		String requestUrl = String.format(
			uriFormat, serviceInstance.getHost(), serviceInstance.getPort()
		);

		// requestBody
		UsernamePassword requestBody = GsonUtils.getGson()
										   .fromJson(parseBodyFromRequest(request), UsernamePassword.class);

		if (logger.isDebugEnabled()) {
			logger.info("login request url and body : [{}], [{}]", requestUrl, GsonUtils.getGson().toJson(requestBody));
		}

		JwtToken token = register(requestUrl, requestBody);

		if (null != token) {
			return token.getToken();
		}
		return null;
	}

	private JwtToken register(String requestUrl, UsernamePassword requestBody) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<>(GsonUtils.getGson().toJson(requestBody), httpHeaders);
		return restTemplate.postForObject(requestUrl, entity, JwtToken.class);
	}

	/**
	 * 从 post 请求中获取请求数据
	 */
	private String parseBodyFromRequest(ServerHttpRequest request) {
		Flux<DataBuffer> body = request.getBody();
		AtomicReference<String> bodyRef = new AtomicReference<>();

		// 订阅缓冲区消费请求体数据
		body.subscribe(
			buffer -> {
				CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
				// 为什么需要释放？否则会出现内存泄漏，之前Cache中使用了retain方法保证缓冲区不被释放
				DataBufferUtils.release(buffer);
				bodyRef.set(charBuffer.toString());
			}
		);
		// request body
		return bodyRef.get();
	}
}
