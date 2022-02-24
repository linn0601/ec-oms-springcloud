package org.linn.web;

import java.io.Serializable;

public class Response<T> implements Serializable {

	/**
	 * <li> 0 表示成功</li>
	 * <Li> 1 表示业务失败</Li>
	 * <li> 401 未认证</li>
	 */
	private int code;

	private String message;

	private T data;

	public Response() {
	}

	public Response(int code) {
		this.code = code;
	}

	public Response(String message, T data) {
		this.message = message;
		this.data = data;
	}

	public Response(int code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public static final Response<Object> OK = new Response<>(0);

	public static Response<Object> getOK() {
		return OK;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
