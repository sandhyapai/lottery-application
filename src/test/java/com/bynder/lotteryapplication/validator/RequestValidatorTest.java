package com.bynder.lotteryapplication.validator;

import com.bynder.lotteryapplication.LotteryPostgresContainer;
import com.bynder.lotteryapplication.dto.QueryWinnerRequest;
import com.bynder.lotteryapplication.dto.RegistrationRequest;
import com.bynder.lotteryapplication.dto.SubmitBallotRequest;
import com.bynder.lotteryapplication.error.ValidationException;
import com.bynder.lotteryapplication.model.LotteryUser;
import com.bynder.lotteryapplication.repository.LotteryUserRepository;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RequestValidatorTest {

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = LotteryPostgresContainer.getInstance();
    @Autowired
    private LotteryUserRepository lotteryUserRepository;

    private RequestValidator requestValidator;

    @BeforeEach
    void init(){
        requestValidator = new RequestValidator(lotteryUserRepository);
        lotteryUserRepository.save(new LotteryUser("exists@email.com", "Abcd123*", "exists"));
        lotteryUserRepository.flush();

    }

    @Test
    void validateValidUser() {
        RegistrationRequest request = new RegistrationRequest("abc@email.com", "abc", "Abc123*a");
        Assertions.assertDoesNotThrow(() -> requestValidator.validateUser(request));
    }

    @Test
    void validateMissingUserNameUser() {
        RegistrationRequest request = new RegistrationRequest(null, "abc", "Abc123*a");
        Assertions.assertThrows(ValidationException.class, () -> requestValidator.validateUser(request));
    }

    @Test
    void validateMissingNameUser() {
        RegistrationRequest request = new RegistrationRequest("abc@email.com", "", "Abc123*a");
        Assertions.assertThrows(ValidationException.class, () -> requestValidator.validateUser(request));
    }

    @Test
    void validateMissingPasswordUser() {
        RegistrationRequest request = new RegistrationRequest("abc@email.com", "abc", "");
        Assertions.assertThrows(ValidationException.class, () -> requestValidator.validateUser(request));
    }

    @Test
    void validateExistingUser() {
        RegistrationRequest request = new RegistrationRequest("exists@email.com", "abc", "Abc123*a");
        Assertions.assertThrows(ValidationException.class, () -> requestValidator.validateUser(request));
    }

    @Test
    void validateInvalidEmailUser() {
        RegistrationRequest request = new RegistrationRequest("abc", "abc", "Abc123*a");
        Assertions.assertThrows(ValidationException.class, () -> requestValidator.validateUser(request));
    }

    @Test
    void validateInvalidPasswordUser() {
        RegistrationRequest request = new RegistrationRequest("abc", "abc", "bc123*a");
        Assertions.assertThrows(ValidationException.class, () -> requestValidator.validateUser(request));
    }

    @Test
    void validateValidBallot() {
        SubmitBallotRequest submitBallotRequest = new SubmitBallotRequest("exists@email.com", "Abcd123*" );
        Assertions.assertDoesNotThrow(() -> requestValidator.validateBallot(submitBallotRequest));
    }

    @Test
    void validateMissingUserNameBallot() {
        SubmitBallotRequest submitBallotRequest = new SubmitBallotRequest("", "Abcd123*" );
        Assertions.assertThrows(ValidationException.class, () -> requestValidator.validateBallot(submitBallotRequest));
    }

    @Test
    void validateMissingPasswordBallot() {
        SubmitBallotRequest submitBallotRequest = new SubmitBallotRequest("exists@email.com", "" );
        Assertions.assertThrows(ValidationException.class, () -> requestValidator.validateBallot(submitBallotRequest));
    }

    @Test
    void validateInvalidUserNameBallot() {
        SubmitBallotRequest submitBallotRequest = new SubmitBallotRequest("doesnotexist@email.com", "Abcd123*" );
        Assertions.assertThrows(ValidationException.class, () -> requestValidator.validateBallot(submitBallotRequest));
    }

    @Test
    void validateInvalidUserPasswordBallot() {
        SubmitBallotRequest submitBallotRequest = new SubmitBallotRequest("exists@email.com", "bcd123*" );
        Assertions.assertThrows(ValidationException.class, () -> requestValidator.validateBallot(submitBallotRequest));
    }

    @Test
    void validateQueryRequest() {
        QueryWinnerRequest queryWinnerRequest = new QueryWinnerRequest(LocalDate.of(2020, 7, 6));
        Assertions.assertDoesNotThrow(() -> requestValidator.validateQueryRequest(queryWinnerRequest));
    }
}