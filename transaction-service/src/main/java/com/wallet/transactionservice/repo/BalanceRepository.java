package com.wallet.transactionservice.repo;

import com.wallet.transactionservice.entity.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BalanceRepository extends JpaRepository<Balance, Long> {

    List<Balance> findAllByUserId(Long userId);

    Optional<Balance> findByUserId(Long userId);

    @Query(nativeQuery = true, value = "select sum(amounts) from balance")
    double findSumAmounts();
}
