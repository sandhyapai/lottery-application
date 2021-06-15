package com.bynder.lotteryapplication.repository;

import com.bynder.lotteryapplication.model.Ballot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BallotRepository  extends JpaRepository<Ballot, Long> {

    List<Ballot> findAllByDate(LocalDate date);
}
