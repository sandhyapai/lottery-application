package com.bynder.lotteryapplication;

import com.bynder.lotteryapplication.repository.BallotRepository;
import com.bynder.lotteryapplication.repository.LotteryWinnerRepository;
import com.bynder.lotteryapplication.repository.LotteryUserRepository;
import com.bynder.lotteryapplication.service.BallotSubmissionService;
import com.bynder.lotteryapplication.service.QueryService;
import com.bynder.lotteryapplication.service.RegistrationService;
import com.bynder.lotteryapplication.validator.RequestValidator;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableConfigurationProperties
@ComponentScan(basePackages = "com.bynder.lotteryapplication")
@EnableScheduling
public class LotteryApplicationConfiguration {

    @Bean
    public RequestValidator createRequestValidator(LotteryUserRepository lotteryUserRepository){
        return new RequestValidator(lotteryUserRepository);
    }

    @Bean
    public RegistrationService createRegistrationService(LotteryUserRepository lotteryUserRepository){
        return new RegistrationService(lotteryUserRepository);
    }

    @Bean
    public QueryService createQueryService(LotteryWinnerRepository lotteryWinnerRepository){
        return new QueryService(lotteryWinnerRepository);
    }

    @Bean
    public BallotSubmissionService createBallotSubmissionService(BallotRepository ballotRepository, LotteryUserRepository lotteryUserRepository){
        return new BallotSubmissionService(ballotRepository, lotteryUserRepository);
    }

}