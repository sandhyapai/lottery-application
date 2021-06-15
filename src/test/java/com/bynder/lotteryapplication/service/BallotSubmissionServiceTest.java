package com.bynder.lotteryapplication.service;

import com.bynder.lotteryapplication.LotteryPostgresContainer;
import com.bynder.lotteryapplication.dto.ApplicationResponse;
import com.bynder.lotteryapplication.dto.SubmitBallotRequest;
import com.bynder.lotteryapplication.error.ApplicationException;
import com.bynder.lotteryapplication.model.LotteryUser;
import com.bynder.lotteryapplication.model.LotteryWinner;
import com.bynder.lotteryapplication.repository.BallotRepository;
import com.bynder.lotteryapplication.repository.LotteryUserRepository;
import com.bynder.lotteryapplication.repository.LotteryWinnerRepository;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BallotSubmissionServiceTest {

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = LotteryPostgresContainer.getInstance();

    @Autowired
    private BallotRepository ballotRepository;

    @Autowired
    private LotteryUserRepository lotteryUserRepository;

    private BallotSubmissionService ballotSubmissionService;

    @BeforeEach
    void init(){
        ballotSubmissionService = new BallotSubmissionService(ballotRepository, lotteryUserRepository);

        lotteryUserRepository.save(new LotteryUser("exists@email.com", "Abcd123*", "exists"));
        lotteryUserRepository.flush();
    }

    @Test
    void validBallotSubmissionTest(){
        SubmitBallotRequest submitBallotRequest = new SubmitBallotRequest("exists@email.com", "Abcd123*");
        ApplicationResponse response = ballotSubmissionService.submitBallot(submitBallotRequest);
        Assert.assertEquals("Ballot submission was successful for the user: exists", response.getMessage());
    }

    @Test
    void validMultipleBallotSubmissionTest(){
        SubmitBallotRequest submitBallotRequest = new SubmitBallotRequest("exists@email.com", "Abcd123*");
        ApplicationResponse response = ballotSubmissionService.submitBallot(submitBallotRequest);
        Assert.assertEquals("Ballot submission was successful for the user: exists", response.getMessage());
    }

    @Test
    void validInvalidBallotSubmissionTest(){
        SubmitBallotRequest submitBallotRequest = new SubmitBallotRequest("random@email.com", "Abcd123*");
        Assertions.assertThrows(ApplicationException.class, () -> ballotSubmissionService.submitBallot(submitBallotRequest));
    }

}