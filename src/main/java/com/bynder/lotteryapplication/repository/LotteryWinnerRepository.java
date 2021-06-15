package com.bynder.lotteryapplication.repository;

import com.bynder.lotteryapplication.model.LotteryWinner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface LotteryWinnerRepository  extends JpaRepository<LotteryWinner, LocalDate> {
}
