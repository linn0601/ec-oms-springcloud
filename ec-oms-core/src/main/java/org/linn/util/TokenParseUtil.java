package org.linn.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Calendar;
import org.linn.rsa.PublicKeyConstant;
import org.linn.vo.UserInfo;

/**
 * 解析token，通过公钥解密
 */
public final class TokenParseUtil {

	private TokenParseUtil() {
	}

	/**
	 * 从jwtToken中解析对象
	 */
	public static UserInfo parseUserInfoToken(String token) {
		if (null == token) {
			return null;
		}

		try {
			Jws<Claims> claimsJws = parseToken(token, getPublicKey());
			Claims body = claimsJws.getBody();
			// 如果已经过期，返回null
			if (body.getExpiration().before(Calendar.getInstance().getTime())) {
				return null;
			}
			return GsonUtils.getGson()
					   .fromJson(body.get(PublicKeyConstant.JWT_USER_INFO_KEY).toString(), UserInfo.class);

		}
		catch (Exception ignore) {
		}
		return null;
	}

	/**
	 * 通过公钥解析jwt token
	 */
	private static Jws<Claims> parseToken(String token, PublicKey publicKey) {
		return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
	}

	private static PublicKey getPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(
			Base64.getDecoder().decode(PublicKeyConstant.PUBLIC_KEY)
		);
		return KeyFactory.getInstance("RSA").generatePublic(keySpec);
	}
}
