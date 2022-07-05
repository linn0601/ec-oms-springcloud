package org.linn.constant;

public class GatewayConstant {

	private GatewayConstant(){
	}

	/**
	 * 网关登陆url
	 */
	public static final String LOGIN_URL = "/login";

	/**
	 * 注册url
	 */
	public static final String REGISTER_URL = "/register";

	/**
	 * 从授权中心获取token的url
	 */
	public static final String AUTHORITY_CENTER_TOKEN_URL= "/auth/token";
}
