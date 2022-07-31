package org.linn.rsa;

public final class PublicKeyConstant {

	private PublicKeyConstant() {
	}

	/**
	 * rsa 公钥
	 */
	public static final String PUBLIC_KEY =
		"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiAHLS7HpSiI9v1a2Mw73WS0p6soJRj"
			+ "ALm577ZwA4bEntJNFqFual494UYaWjJ8fI+bVGt/s00qV05oRv4aol9Gw5XgmWDwax3R"
			+ "BesxYOhdNlPgbV87WOXQ/PFnUsiAPsjOk+96i7tabEDa58mATPZyYZDRfYQYKbRmG9KQ"
			+ "z1KqhAXI0k/5E6sElPfxc4e/gey3Ysdfyl6PwNuN29VbvqFuhAocPqneBL62CsThBdy4"
			+ "8xVdKeHiAAlj6OsjryJESS3rzSovA4VjF+WVgDqCh2Ocw8dv8MMZ7A0nwA/WktUZsTWl"
			+ "/cVUUlCy5RfORCfVNfHCjB3leb+htHYZn2yJ7BUwIDAQAB";

	/**
	 * jwt中存储用户信息的 key
	 */
	public static final String JWT_USER_INFO_KEY = "ec-oms-token";

	/**
	 * 授权中心的 service-id
	 */
	public static final String AUTHORITY_CENTER_SERVICE_ID = "ec-oms-authority-center";
}
