package org.linn.service;

import org.linn.vo.UsernamePassword;

public interface JWTService {

	String generateToken(String username, String password) throws Exception;

	String generateToken(String username, String password, int expire) throws Exception;

	String registerUserAndGenerateToken(UsernamePassword usernamePassword) throws Exception;
}
