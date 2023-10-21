package org.linn.service;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.linn.entity.User;
import org.linn.vo.UsernamePassword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class UserControllerTest {
	public static final Logger logger = LoggerFactory.getLogger(UserControllerTest.class);

	@Autowired
	private JwtService jwtService;

	@Autowired
	private UserRepository userRepository;

	@Test
	void createUserRecord(){
//		User user = new User();
//		user.setUsername("李超");
//		user.setPassword("123");
//		user.setExtraInfo("{}");
//		userRepository.save(user);
		Optional<User> user = userRepository.findById(2L);
		logger.info("user: {}", new Gson().toJson(user.get()));
	}

	@Test
	void test() throws Exception {

		UsernamePassword usernamePassword = new UsernamePassword();
		usernamePassword.setPassword("linn");
		usernamePassword.setUsername("linn");
		String token = jwtService.registerUserAndGenerateToken(usernamePassword);
		System.out.println(token);
	}
}
