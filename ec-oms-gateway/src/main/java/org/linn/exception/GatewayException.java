package org.linn.exception;

/**
 * 网关异常
 */
public class GatewayException extends RuntimeException {

	private String code;

	private String message;

	public GatewayException(String message) {
		this.code = "10001";
		this.message = message;
	}

	public GatewayException(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
