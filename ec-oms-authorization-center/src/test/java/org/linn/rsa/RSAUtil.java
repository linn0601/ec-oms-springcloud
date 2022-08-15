package org.linn.rsa;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.springframework.util.Base64Utils;

/**
 * 生成公钥和私钥的工具
 */
public final class RSAUtil {

	public static void main(String[] args) throws NoSuchAlgorithmException {
		generateKeyBytes();
	}

	public static void generateKeyBytes() throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(2048);

		KeyPair keyPair = keyPairGenerator.generateKeyPair();

		PrivateKey pairPrivate = keyPair.getPrivate();
		PublicKey pairPublic = keyPair.getPublic();

		String publicKey = Base64Utils.encodeToString(pairPublic.getEncoded());
		String privateKey = Base64Utils.encodeToString(pairPrivate.getEncoded());

		System.out.println("公钥:" + publicKey);
		System.out.println("私钥:" + privateKey);
	}
}
