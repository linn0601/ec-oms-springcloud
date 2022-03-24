package org.linn;

import org.junit.jupiter.api.Test;
import org.linn.entity.User;
import org.linn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserControllerTest {

	@Autowired
	private UserService userService;

	@Test
	void test() {
		User user = new User();
		user.setUsername("linn");
		user.setPassword("chao");
		userService.save(user);
	}
}
