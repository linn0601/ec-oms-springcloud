package org.linn.advice;

import javax.servlet.http.HttpServletRequest;
import org.linn.resp.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * 全局异常捕获处理
 */
@RestControllerAdvice
@SuppressWarnings("ALL")
public class GlobExceptionAdvice extends ResponseEntityExceptionHandler {

	@SuppressWarnings("java:S2387")
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

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
																  HttpHeaders headers,
																  HttpStatus status,
																  WebRequest request) {

		StringBuilder builder = new StringBuilder();
		ex.getBindingResult().getAllErrors().forEach(error -> builder.append(error.getDefaultMessage()));

		Response<Object> response = new Response<>();
		response.setCode(1);
		response.setMessage(builder.toString());
		return handleExceptionInternal(ex, response, headers, HttpStatus.OK, request);
	}

}
