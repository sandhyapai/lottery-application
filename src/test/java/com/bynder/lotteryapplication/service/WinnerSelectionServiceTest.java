package com.bynder.lotteryapplication.service;

import com.bynder.lotteryapplication.LotteryPostgresContainer;
import com.bynder.lotteryapplication.model.Ballot;
import com.bynder.lotteryapplication.model.LotteryUser;
import com.bynder.lotteryapplication.model.LotteryWinner;
import com.bynder.lotteryapplication.repository.BallotRepository;
import com.bynder.lotteryapplication.repository.LotteryUserRepository;
import com.bynder.lotteryapplication.repository.LotteryWinnerRepository;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class WinnerSelectionServiceTest {

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = LotteryPostgresContainer.getInstance();

    @Autowired
    private BallotRepository ballotRepository;

    @Autowired
    private LotteryWinnerRepository lotteryWinnerRepository;

    @Autowired
    private LotteryUserRepository lotteryUserRepository;

    private WinnerSelectionService winnerSelectionService;

    @BeforeEach
    void init(){

        winnerSelectionService = new WinnerSelectionService(ballotRepository, lotteryWinnerRepository);;
    }

    @Test
    void validWinnerSelection(){
        populateTestData();
        winnerSelectionService.selectWinner();
        Optional<LotteryWinner> winner = lotteryWinnerRepository.findById(LocalDate.now());
        Assert.assertTrue(winner.isPresent());
    }

    @Test
    void validWinnerSelectionWithNoBallots(){
        winnerSelectionService.selectWinner();
        Optional<LotteryWinner> winner = lotteryWinnerRepository.findById(LocalDate.now());
        Assert.assertTrue(!winner.isPresent());
    }

    @Test
    public void testScheduler(){
        org.springframework.scheduling.support.CronTrigger trigger =
                new CronTrigger("0 0 0 * * *");
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);

        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss EEEE");
        final Date yesterday = today.getTime();
        Date nextExecutionTime = trigger.nextExecutionTime(
                new TriggerContext() {

                    @Override
                    public Date lastScheduledExecutionTime() {
                        return yesterday;
                    }

                    @Override
                    public Date lastActualExecutionTime() {
                        return yesterday;
                    }

                    @Override
                    public Date lastCompletionTime() {
                        return yesterday;
                    }
                });

        String message = "Next Execution date: " + df.format(nextExecutionTime);
        Assert.assertTrue(message.contains("00:00:00"));
        Assert.assertTrue(nextExecutionTime.after(yesterday));

    }


    void populateTestData(){
        LotteryUser exists = new LotteryUser("exists@email.com", "Abcd123*", "exists");
        LotteryUser abc = new LotteryUser("abc@email.com", "Abcd123*", "abc");
        LotteryUser xyz = new LotteryUser("xyz@email.com", "Abcd123*", "xyz");
        lotteryUserRepository.save(exists);
        lotteryUserRepository.save(abc);
        lotteryUserRepository.save(xyz);
        lotteryUserRepository.flush();

        ballotRepository.save(new Ballot(exists));
        ballotRepository.save(new Ballot(exists));
        ballotRepository.save(new Ballot(abc));
        ballotRepository.save(new Ballot(xyz));
        ballotRepository.save(new Ballot(xyz));
        ballotRepository.save(new Ballot(xyz));
        ballotRepository.flush();
    }

}