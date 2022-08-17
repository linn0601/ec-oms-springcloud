package org.linn.vo;

import java.io.Serializable;

/**
 * 全局的用户认证对象
 */
public class JwtToken implements Serializable {

	private static final long serialVersionUID = 1643053691155161871L;

	private String token;

	public JwtToken(String token) {
		this.token = token;
	}

	public JwtToken() {
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
