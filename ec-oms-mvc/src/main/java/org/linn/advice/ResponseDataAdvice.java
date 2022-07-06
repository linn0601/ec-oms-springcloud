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

@RestControllerAdvice(value = "org.linn")
public class ResponseDataAdvice implements ResponseBodyAdvice<Object> {

	/**
	 * 是否对响应进行处理
	 */
	@Override
	public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterType) {
		if (methodParameter.getDeclaringClass().isAnnotationPresent(IgnoreResponseAdvice.class)) {
			return false;
		}

		Method requireNonNull = Objects.requireNonNull(methodParameter.getMethod());

		return !requireNonNull.isAnnotationPresent(IgnoreResponseAdvice.class);
	}

	@SuppressWarnings("NullableProblems")
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
