package org.linn.service.impl;

import org.linn.service.JwtService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class JwtServiceImpl implements JwtService {
	@Override
	public String generateToken(String username, String password) {
		return null;
	}

	@Override
	public String generateToken(String username, String password, int expire) {
		return null;
	}

	@Override
	public String registerUserAndGenerateToken() {
		return null;
	}
}
