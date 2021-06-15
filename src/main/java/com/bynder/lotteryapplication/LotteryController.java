package com.bynder.lotteryapplication;

import com.bynder.lotteryapplication.dto.*;
import com.bynder.lotteryapplication.error.ApplicationException;
import com.bynder.lotteryapplication.error.ErrorCode;
import com.bynder.lotteryapplication.error.ErrorResponse;
import com.bynder.lotteryapplication.error.ValidationException;
import com.bynder.lotteryapplication.service.BallotSubmissionService;
import com.bynder.lotteryapplication.service.QueryService;
import com.bynder.lotteryapplication.service.RegistrationService;
import com.bynder.lotteryapplication.validator.RequestValidator;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@EntityScan("com.bynder")
@RestController
@Slf4j
public class LotteryController {

    private final RequestValidator requestValidator;

    @Autowired
    private final RegistrationService registrationService;

    private final BallotSubmissionService ballotSubmissionService;

    private final QueryService queryService;

    public LotteryController(@NonNull RequestValidator requestValidator,
                             @NonNull RegistrationService registrationService,
                             @NonNull BallotSubmissionService ballotSubmissionService,
                             @NonNull QueryService queryService){

        this.requestValidator = requestValidator;
        this.registrationService = registrationService;
        this.ballotSubmissionService = ballotSubmissionService;
        this.queryService = queryService;
    }


    @PostMapping(path = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApplicationResponse> registerUser(@RequestBody @NonNull RegistrationRequest request) throws ValidationException, ApplicationException {
        log.error(request.getUserName());
        requestValidator.validateUser(request);
        ApplicationResponse response = registrationService.register(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(path = "/submit-ballot", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApplicationResponse> submitBallot(@RequestBody @NonNull SubmitBallotRequest request) throws ValidationException, ApplicationException{
        requestValidator.validateBallot(request);
        ApplicationResponse response = ballotSubmissionService.submitBallot(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(path = "/query-winner", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApplicationResponse> queryWinner(@RequestBody @NonNull QueryWinnerRequest request) throws  ValidationException, ApplicationException{
        requestValidator.validateQueryRequest(request);
        ApplicationResponse response = queryService.queryWinner(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleExceptions(Exception exception, HttpServletRequest request){
        if(exception instanceof ValidationException){
            ErrorResponse response = new ErrorResponse(((ValidationException) exception).getErrorCode(), ((ValidationException) exception).getErrorMessage());
            return ResponseEntity
                    .badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        }

        if(exception instanceof ApplicationException){
            ErrorResponse response = new ErrorResponse(((ApplicationException) exception).getErrorCode(), ((ApplicationException) exception).getErrorMessage());
            return ResponseEntity
                    .internalServerError()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        }

        log.error(exception.getMessage());
        ErrorResponse response = new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR, "Unexpected error occurred while processing your request");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
