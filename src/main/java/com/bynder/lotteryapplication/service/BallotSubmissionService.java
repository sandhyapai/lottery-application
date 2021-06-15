package com.bynder.lotteryapplication.service;

import com.bynder.lotteryapplication.dto.ApplicationResponse;
import com.bynder.lotteryapplication.dto.SubmitBallotRequest;
import com.bynder.lotteryapplication.error.ApplicationException;
import com.bynder.lotteryapplication.error.ErrorCode;
import com.bynder.lotteryapplication.model.Ballot;
import com.bynder.lotteryapplication.model.LotteryUser;
import com.bynder.lotteryapplication.repository.BallotRepository;
import com.bynder.lotteryapplication.repository.LotteryUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Slf4j
public class BallotSubmissionService {

    @Autowired
    private final BallotRepository ballotRepository;
    @Autowired
    private final LotteryUserRepository lotteryUserRepository;

    public ApplicationResponse submitBallot(SubmitBallotRequest request) throws ApplicationException{

        Optional<LotteryUser> lotteryUser = lotteryUserRepository.findById(request.getUserName());
        if(lotteryUser.isPresent()){
            log.info("Trying to save the ballot to database");
            Ballot ballot = ballotRepository.save(new Ballot(lotteryUser.get()));

            if(Objects.isNull(ballot)){
                log.error("The save operation failed for the userName: " + request.getUserName());
                throw new ApplicationException(ErrorCode.FAILED_BALLOT_SUBMISSION, "An error occurred file submitting the ballot for today's lottery");
            }
            else{
                return new ApplicationResponse("Ballot submission was successful for the user: " + ballot.getLotteryUser().getName());
            }
        }
        else{

            log.error("The user with username: " + request.getUserName() + "could not be found");
            throw new ApplicationException(ErrorCode.COULD_NOT_FIND_USER, "Failed to find the corresponding registered user");
        }

    }
}
