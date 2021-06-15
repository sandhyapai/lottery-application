package com.bynder.lotteryapplication.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class QueryWinnerRequest {

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDate date;

    @JsonCreator
    public QueryWinnerRequest(@JsonProperty("date") final LocalDate date){
        this.date = date;
    }
}
