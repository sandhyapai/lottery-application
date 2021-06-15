package com.bynder.lotteryapplication.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class SubmitBallotRequest {

    private final String userName;

    private final String password;

    @JsonCreator
    public SubmitBallotRequest(@JsonProperty("userName") final String userName,
                               @JsonProperty("password") final String password){
        this.userName = userName;
        this.password = password;
    }
}
