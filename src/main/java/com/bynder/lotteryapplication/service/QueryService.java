package com.bynder.lotteryapplication.service;

import com.bynder.lotteryapplication.dto.ApplicationResponse;
import com.bynder.lotteryapplication.dto.QueryWinnerRequest;
import com.bynder.lotteryapplication.error.ApplicationException;
import com.bynder.lotteryapplication.error.ErrorCode;
import com.bynder.lotteryapplication.model.LotteryWinner;
import com.bynder.lotteryapplication.repository.LotteryWinnerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.PersistenceException;
import java.util.Optional;

@AllArgsConstructor
@Slf4j
public class QueryService {

    @Autowired
    private final LotteryWinnerRepository lotteryWinnerRepository;
    public ApplicationResponse queryWinner(QueryWinnerRequest request) throws ApplicationException {

        try {
            Optional<LotteryWinner> winner = lotteryWinnerRepository.findById(request.getDate());
            if (winner.isPresent()) {
                final String message = "The winner for the date: " + winner.get().getDate() + " is user: " + winner.get().getWinner().getName();
                return new ApplicationResponse(message);
            } else {
                log.error("Unable to find winner for the date: " + request.getDate());
                throw new ApplicationException(ErrorCode.FAILED_QUERY, "Could not fetch the winner for the date: " + request.getDate());
            }
        }catch(PersistenceException exception){
            log.error("Unable to find winner for the date: " + request.getDate());
            throw new ApplicationException(ErrorCode.FAILED_QUERY, "Could not fetch the winner for the date: " + request.getDate());
        }
    }
}
