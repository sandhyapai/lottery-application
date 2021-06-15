package com.bynder.lotteryapplication.service;

import com.bynder.lotteryapplication.LotteryPostgresContainer;
import com.bynder.lotteryapplication.dto.ApplicationResponse;
import com.bynder.lotteryapplication.dto.QueryWinnerRequest;
import com.bynder.lotteryapplication.error.ApplicationException;
import com.bynder.lotteryapplication.model.LotteryUser;
import com.bynder.lotteryapplication.model.LotteryWinner;
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
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class QueryServiceTest {

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = LotteryPostgresContainer.getInstance();

    @Autowired
    private LotteryWinnerRepository lotteryWinnerRepository;

    @Autowired
    private LotteryUserRepository lotteryUserRepository;

    private QueryService queryService;

    @BeforeEach
    void init(){
        queryService = new QueryService(lotteryWinnerRepository);

        lotteryUserRepository.save(new LotteryUser("exists@email.com", "Abcd123*", "exists"));
        lotteryUserRepository.flush();
        lotteryWinnerRepository.save(new LotteryWinner( LocalDate.of(2020, 7, 6),
                                        new LotteryUser("exists@email.com", "Abcd123*", "exists")));
        lotteryWinnerRepository.flush();

    }

    @Test
    void findWinnerForDate(){
        QueryWinnerRequest queryWinnerRequest = new QueryWinnerRequest(LocalDate.of(2020, 7, 6));
        ApplicationResponse response = queryService.queryWinner(queryWinnerRequest);
        Assert.assertEquals("The winner for the date: 2020-07-06 is user: exists", response.getMessage());
    }

    @Test
    void findWinnerForNonExistentDate(){
        QueryWinnerRequest queryWinnerRequest = new QueryWinnerRequest(LocalDate.of(2020, 8, 6));
        Assertions.assertThrows(ApplicationException.class, () -> queryService.queryWinner(queryWinnerRequest));
    }

}