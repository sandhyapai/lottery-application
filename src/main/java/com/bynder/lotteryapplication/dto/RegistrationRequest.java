package com.bynder.lotteryapplication.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import static java.util.Objects.isNull;


@Slf4j
@Value
public class RegistrationRequest {

    private String userName;

    private String name;

    private String password;

    @JsonCreator
    public RegistrationRequest(@JsonProperty("userName") final String userName,
                               @JsonProperty("name") final String name,
                               @JsonProperty("password") final String password){
        this.userName = userName;
        this.name = name;
        this.password = password;
    }
}

