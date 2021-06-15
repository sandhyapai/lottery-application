package com.bynder.lotteryapplication.repository;

import com.bynder.lotteryapplication.model.LotteryUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface LotteryUserRepository extends JpaRepository<LotteryUser, String> {

}
