package com.wallet.transactionservice.repo;

import com.wallet.transactionservice.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByUserId(Long userId);

    @Query(nativeQuery = true, value = "SELECT count(*) FROM transaction WHERE datetime > NOW() - INTERVAL '15 minutes'")
    int findTransactionsBefore15Minutes();

}
