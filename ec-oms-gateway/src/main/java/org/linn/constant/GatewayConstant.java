package org.linn.constant;

public class GatewayConstant {

	private GatewayConstant(){
	}

	/**
	 * 网关登陆url
	 */
	public static final String LOGIN_URL = "/ec-oms/auth/login";

	/**
	 * 注册url
	 */
	public static final String REGISTER_URL = "/ec-oms/auth/register";

	/**
	 * 从授权中心获取token的url
	 */
	public static final String AUTHORITY_CENTER_TOKEN_URL_FORMAT =
		"http://%s:%s/ec-oms-authorization-center/auth/token";

	/**
	 * 去授权中心注册并拿到token的 uri 格式化接口
	 */
	public static final String AUTHORITY_CENTER_REGISTER_URL_FORMAT =
		"http://%s:%s/ec-oms-authorization-center/auth/register";
}
