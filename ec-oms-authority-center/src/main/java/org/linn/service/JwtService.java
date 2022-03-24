package org.linn.service;

public interface JwtService {

	String generateToken(String username, String password);

	String generateToken(String username, String password, int expire);

	String registerUserAndGenerateToken();
}
