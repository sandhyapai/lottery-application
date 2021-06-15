package com.bynder.lotteryapplication.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="ballot")
@Data
@NoArgsConstructor
public class Ballot {

    @Id
    @GeneratedValue
    private long ballotId;
    private LocalDate date;
    @ManyToOne
    private LotteryUser lotteryUser;

    public Ballot(LotteryUser lotteryUser) {
        this.date = LocalDate.now();
        this.lotteryUser = lotteryUser;
    }
}
