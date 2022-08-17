package org.linn.service;

import org.junit.jupiter.api.Test;
import org.linn.vo.UsernamePassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserControllerTest {

	@Autowired
	private JwtService jwtService;

	@Test
	void test() throws Exception {

		UsernamePassword usernamePassword = new UsernamePassword();
		usernamePassword.setPassword("linn");
		usernamePassword.setUsername("linn");
		String token = jwtService.registerUserAndGenerateToken(usernamePassword);
		System.out.println(token);
	}
}
