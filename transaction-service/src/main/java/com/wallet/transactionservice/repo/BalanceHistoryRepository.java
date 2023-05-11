package com.wallet.transactionservice.repo;

import com.wallet.transactionservice.entity.BalanceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BalanceHistoryRepository extends JpaRepository<BalanceHistory, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM balance_history WHERE local_date_time > CURRENT_DATE - INTERVAL '1 day'")
    List<BalanceHistory> findLastHistory();


}
