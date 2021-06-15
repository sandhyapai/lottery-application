package com.bynder.lotteryapplication.service;

import com.bynder.lotteryapplication.LotteryPostgresContainer;
import com.bynder.lotteryapplication.dto.RegistrationRequest;
import com.bynder.lotteryapplication.error.ApplicationException;
import com.bynder.lotteryapplication.model.LotteryUser;
import com.bynder.lotteryapplication.repository.LotteryUserRepository;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RegistrationServiceTest {

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = LotteryPostgresContainer.getInstance();

    @Autowired
    private LotteryUserRepository lotteryUserRepository;

    private RegistrationService registrationService;

    @BeforeEach
    void init(){
        registrationService = new RegistrationService(lotteryUserRepository);
        lotteryUserRepository.save(new LotteryUser("exists@email.com", "Abcd123*", "exists"));
        lotteryUserRepository.flush();

    }

    @Test
    void validateNewUserIsCreated(){
        RegistrationRequest request = new RegistrationRequest("abc@email.com", "abc", "Abc123*a");
        Assertions.assertDoesNotThrow(() -> registrationService.register(request));
        Optional<LotteryUser> optionalLotteryUser = lotteryUserRepository.findById(request.getUserName());
        LotteryUser lotteryUser = optionalLotteryUser.orElse(null);
        Assert.assertNotNull(lotteryUser);
        Assert.assertEquals(request.getUserName(), lotteryUser.getUserName());
        Assert.assertEquals(request.getName(), lotteryUser.getName());
        Assert.assertEquals(request.getPassword(), lotteryUser.getPassword());

    }

    @Test
    void validateUserAlreadyExists(){
        RegistrationRequest request = new RegistrationRequest("exists@email.com", "exists", "Abc123*");
        Assertions.assertThrows(ApplicationException.class, () -> registrationService.register(request));

    }

}