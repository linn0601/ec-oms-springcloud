package org.linn.advice;

import java.lang.reflect.Method;
import java.util.Objects;
import org.linn.resp.Response;
import org.linn.annotation.IgnoreResponseAdvice;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 通过实现 ResponseBodyAdvice 对所有Response响应进行拦截
 * <li>value 可以限定包的范围</li>
 */
@RestControllerAdvice(value = "org.linn")
public class ResponseDataAdvice implements ResponseBodyAdvice<Object> {

	/**
	 * 是否对响应进行处理
	 */
	@SuppressWarnings("all")
	@Override
	public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterType) {
		// 获取到这个方法对应的类是否有指定注解
		if (methodParameter.getDeclaringClass().isAnnotationPresent(IgnoreResponseAdvice.class)) {
			return false;
		}
		Method requireNonNull = Objects.requireNonNull(methodParameter.getMethod());
		// 获取这个方法是否有指定注解
		return !requireNonNull.isAnnotationPresent(IgnoreResponseAdvice.class);
	}
 	/**
	 * 在响应给客户端之前增加的处理逻辑
	 *
	 * @return 最终返回对象
	 */
	@SuppressWarnings("all")
	@Override
	public Object beforeBodyWrite(Object obj, MethodParameter returnType, MediaType selectedContentType,
								  Class<? extends HttpMessageConverter<?>> selectedConverterType,
								  ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {

		// 定义最终返回对象
		Response<Object> response = new Response<>();

		if (Objects.isNull(obj)) {
			return response;
		}
		else if (obj instanceof Response) {
			response = (Response<Object>) obj;
		}
		else {
			response.setData(obj);
		}
		return response;
	}
}
