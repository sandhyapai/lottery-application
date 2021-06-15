package com.bynder.lotteryapplication.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name="lotteryWinner")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LotteryWinner {

    @Id
    private LocalDate date;

    @OneToOne
    private LotteryUser winner;
}
