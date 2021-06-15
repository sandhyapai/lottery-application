package com.bynder.lotteryapplication.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@Getter
public class ApplicationException extends RuntimeException{

    private final ErrorCode errorCode;
    private final String errorMessage;
}
