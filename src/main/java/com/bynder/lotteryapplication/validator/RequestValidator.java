package com.bynder.lotteryapplication.validator;

import antlr.StringUtils;
import com.bynder.lotteryapplication.dto.QueryWinnerRequest;
import com.bynder.lotteryapplication.dto.RegistrationRequest;
import com.bynder.lotteryapplication.dto.SubmitBallotRequest;
import com.bynder.lotteryapplication.error.ErrorCode;
import com.bynder.lotteryapplication.error.ValidationException;
import com.bynder.lotteryapplication.model.LotteryUser;
import com.bynder.lotteryapplication.repository.LotteryUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor
public class RequestValidator {

    @Autowired
    private final LotteryUserRepository lotteryUserRepository;

    private static final Pattern VALID_EMAIL_PATTERN = Pattern.compile("^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");

    private static final Pattern VALID_PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,10}$");
    
    public void validateUser(RegistrationRequest request) {

        if(Objects.isNull(request.getUserName()) || request.getUserName().isEmpty()){
            throw new ValidationException(ErrorCode.MISSING_USER_NAME, "Request is missing the user name field");
        }

        if(Objects.isNull(request.getPassword()) || request.getPassword().isEmpty()){
            throw new ValidationException(ErrorCode.MISSING_PASSWORD, "Request is missing the password field");
        }

        if(Objects.isNull(request.getName()) || request.getName().isEmpty()){
            throw new ValidationException(ErrorCode.MISSING_NAME, "Request is missing the name field");
        }

        if(lotteryUserRepository.existsById(request.getUserName())){
            throw new ValidationException(ErrorCode.INVALID_USER, "The user you are trying to create already exists");
        }

        Matcher emailMatcher = VALID_EMAIL_PATTERN.matcher(request.getUserName());
        if(!emailMatcher.matches()){
            throw new ValidationException(ErrorCode.INVALID_USER, "The user name is not a valid email address");
        }

        Matcher passwordMatcher = VALID_PASSWORD_PATTERN.matcher(request.getPassword());
        if(!passwordMatcher.matches()){
            throw new ValidationException(ErrorCode.INVALID_USER, "The password must contain minimum eight and maximum 10 characters, at least one uppercase letter, one lowercase letter, one number and one special character");
        }

    }

    public void validateBallot(SubmitBallotRequest request) {
        if(Objects.isNull(request.getUserName()) || request.getUserName().isEmpty()){
            throw new ValidationException(ErrorCode.MISSING_USER_NAME, "Request is missing the user name field");
        }

        if(Objects.isNull(request.getPassword()) || request.getPassword().isEmpty()){
            throw new ValidationException(ErrorCode.MISSING_PASSWORD, "Request is missing the password field");
        }

        Matcher emailMatcher = VALID_EMAIL_PATTERN.matcher(request.getUserName());
        if(!emailMatcher.matches()){
            throw new ValidationException(ErrorCode.INVALID_USER, "The user name is not a valid email address");
        }

        try {
            LotteryUser user = lotteryUserRepository.getById(request.getUserName());
            if(! request.getPassword().equals(user.getPassword())){
                throw new ValidationException(ErrorCode.INVALID_USER, "Unable to find the specified user. Check username and password");
            }
        }
        catch (EntityNotFoundException exception){
            throw new ValidationException(ErrorCode.INVALID_USER, "Unable to find the specified user");
        }


    }

    public void validateQueryRequest(QueryWinnerRequest request) {

        if(Objects.isNull(request.getDate())){
            throw new ValidationException(ErrorCode.MISSING_DATE, "Request is missing the date field");
        }
        if(request.getDate().isAfter(LocalDate.now()) || request.getDate().equals(LocalDate.now())){
            throw new ValidationException(ErrorCode.INVALID_DATE, "Date should be older than today to fetch the winner");
        }
    }
}
