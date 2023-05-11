package com.wallet.transactionservice.repo;

import com.wallet.transactionservice.entity.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM transaction_history WHERE datetime > CURRENT_DATE - INTERVAL '1 day'")
    List<TransactionHistory> findLastHistory();
}
