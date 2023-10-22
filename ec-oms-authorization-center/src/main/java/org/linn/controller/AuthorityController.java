package org.linn.controller;

import org.linn.annotation.IgnoreResponseAdvice;
import org.linn.service.JWTService;
import org.linn.vo.JwtToken;
import org.linn.vo.UsernamePassword;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthorityController {

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
		return new JwtToken(token);
	}

	/**
	 * 注册
	 */
	@PostMapping("/register")
	@IgnoreResponseAdvice
	public JwtToken register(@RequestBody UsernamePassword usernamePassword) throws Exception {
		String token = jwtService.registerUserAndGenerateToken(usernamePassword);
		return new JwtToken(token);
	}

}
