package com.bynder.lotteryapplication.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class ValidationException  extends  IllegalArgumentException{

    private ErrorCode errorCode;
    private String errorMessage;
}
