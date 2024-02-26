package org.linn.controller;

import org.linn.annotation.IgnoreResponseAdvice;
import org.linn.service.JWTService;
import org.linn.vo.JwtToken;
import org.linn.vo.UsernamePassword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthorityController {

	private static final Logger log = LoggerFactory.getLogger(AuthorityController.class);

	private final JWTService jwtService;

	public AuthorityController(JWTService jwtService) {
		this.jwtService = jwtService;
	}

	/**
	 * 从授权中心获取token
	 */
	@PostMapping("/token")
	@IgnoreResponseAdvice
	public JwtToken token(@RequestBody UsernamePassword usernamePassword) throws Exception {
		String token = jwtService.generateToken(usernamePassword.getUsername(), usernamePassword.getPassword());
		log.info("token: {}", token);
		return new JwtToken(token);
	}

	/**
	 * 注册
	 */
	@PostMapping("/register")
	@IgnoreResponseAdvice
	public JwtToken register(@RequestBody UsernamePassword usernamePassword) throws Exception {
		String token = jwtService.registerUserAndGenerateToken(usernamePassword);
		log.info("token: {}", token);
		return new JwtToken(token);
	}

}
