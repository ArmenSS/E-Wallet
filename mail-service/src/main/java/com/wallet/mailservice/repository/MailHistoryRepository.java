package com.wallet.mailservice.repository;

import com.wallet.mailservice.entity.MailHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MailHistoryRepository extends JpaRepository<MailHistory,Long> {

    Optional<MailHistory> findByUserEmail(String email);
}
