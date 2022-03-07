package org.linn.web;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常捕获处理
 */
@RestControllerAdvice
public class GlobExceptionAdvice {

	private static final Logger logger = LoggerFactory.getLogger(GlobExceptionAdvice.class);

	@ExceptionHandler(value = Exception.class)
	public Response<String> handlerException(HttpServletRequest request, Exception exception) {

		long errorCode = System.currentTimeMillis();

		Response<String> response = new Response<>();
		response.setCode(1);
		response.setMessage("code：" + errorCode + " message：" + exception.getMessage());

		logger.info("error code：{}，message：{}", errorCode, exception.getMessage());
		return response;
	}

}
