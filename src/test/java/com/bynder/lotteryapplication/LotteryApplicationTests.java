package com.bynder.lotteryapplication;

import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest
@Configuration
class LotteryApplicationTests {

	@ClassRule
	public static PostgreSQLContainer postgreSQLContainer = LotteryPostgresContainer.getInstance();

	@Test
	void contextLoads() {

	}

}
