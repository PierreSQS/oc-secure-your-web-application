package com.openclassrooms.springsecurity;

import com.openclassrooms.springsecurity.controllers.LoginController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SpringSecurityAuthApplicationTests {

	@Autowired
	LoginController loginController;

	@Test
	void contextLoads(ApplicationContext appCtx) {
		assertThat(appCtx).isNotNull();
	}

	@Test
	void controllerIsInContext() {
		assertThat(loginController).isNotNull();
	}

}
