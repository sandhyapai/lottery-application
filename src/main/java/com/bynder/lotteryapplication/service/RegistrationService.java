package com.bynder.lotteryapplication.service;

import com.bynder.lotteryapplication.dto.ApplicationResponse;
import com.bynder.lotteryapplication.dto.RegistrationRequest;
import com.bynder.lotteryapplication.error.ApplicationException;
import com.bynder.lotteryapplication.error.ErrorCode;
import com.bynder.lotteryapplication.model.LotteryUser;
import com.bynder.lotteryapplication.repository.LotteryUserRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import java.util.Objects;

@AllArgsConstructor
@Service
@Slf4j
public class RegistrationService {

    @Autowired
    private final LotteryUserRepository lotteryUserRepository;

    public ApplicationResponse register(@NonNull RegistrationRequest request) throws ApplicationException{

        try{
            LotteryUser lotteryUser = lotteryUserRepository.save(new LotteryUser(request.getUserName(), request.getPassword(), request.getName()));
            lotteryUserRepository.flush();

            log.info("Created a user with user name: " + request.getUserName());
            return new ApplicationResponse("Registration was successful for the lotteryUser: " + lotteryUser.getName() );
        }
        catch(DataIntegrityViolationException exception){
            log.error("Registration failed for the user with userName: " + request.getUserName());
            throw new ApplicationException(ErrorCode.FAILED_REGISTRATION, "The user trying to create already exists");

        }
        catch(PersistenceException exception) {
            log.error("Registration failed for the user with userName: " + request.getUserName());
            throw new ApplicationException(ErrorCode.FAILED_REGISTRATION, "An error occurred while registering the lotteryUser");
        }

    }
}
