package com.bynder.lotteryapplication.service;

import com.bynder.lotteryapplication.model.Ballot;
import com.bynder.lotteryapplication.model.LotteryWinner;
import com.bynder.lotteryapplication.repository.BallotRepository;
import com.bynder.lotteryapplication.repository.LotteryWinnerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class WinnerSelectionService {
    @Autowired
    private final BallotRepository ballotRepository;

    @Autowired
    private final LotteryWinnerRepository lotteryWinnerRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void selectWinner(){

        final LocalDate today = LocalDate.now();
        log.info("Selecting the winner for the date: " + today);

        //collect all the ballots for the given date
        List<Ballot> ballots = ballotRepository.findAllByDate(today);

        if(!ballots.isEmpty()) {
            List<Long> ballotIds = ballots.stream().map(Ballot::getBallotId).collect(Collectors.toList());

            //choose a random winner id
            Random random = new Random();
            final long winnerId = ballotIds.get(random.nextInt(ballotIds.size()));

            //store the winner id
            Ballot winningBallot = ballots.stream().filter(ballot -> ballot.getBallotId() == winnerId).findAny().get();
            LotteryWinner winner = lotteryWinnerRepository.save(new LotteryWinner(today, winningBallot.getLotteryUser()));

            if (Objects.isNull(winner)) {
                log.error("an error occurred while saving the winner for the date: " + today);
            }
        }
        else{
            log.error("Could not find any ballots for the date: " + today);
        }

    }

}
