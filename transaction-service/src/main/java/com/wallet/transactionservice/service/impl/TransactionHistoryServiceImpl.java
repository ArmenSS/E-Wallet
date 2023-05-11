package com.wallet.transactionservice.service.impl;

import com.wallet.transactionservice.dto.TransactionHistoryDto;
import com.wallet.transactionservice.entity.TransactionHistory;
import com.wallet.transactionservice.mapper.TransactionHistoryMapper;
import com.wallet.transactionservice.repo.TransactionHistoryRepository;
import com.wallet.transactionservice.repo.TransactionRepository;
import com.wallet.transactionservice.service.TransactionHistoryService;
import com.wallet.userservice.entity.UserEntity;
import com.wallet.userservice.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionHistoryServiceImpl implements TransactionHistoryService {

    private final TransactionRepository transactionRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;
    private final TransactionHistoryMapper transactionHistoryMapper;

    @Override
    public List<TransactionHistoryDto> findTransactionsCountForLastHours(UserEntity userEntity) {
        if (userEntity.getUserRole().equals(UserRole.ADMIN)) {
            return transactionHistoryMapper.toDto(transactionHistoryRepository.findLastHistory());
        }
        return null;
    }

    @Override
    @Scheduled(cron = "0 15/15 * * * *")
    public void save() {
        int count = transactionRepository.findTransactionsBefore15Minutes();
        TransactionHistory build = TransactionHistory.builder()
                .count(count)
                .datetime(LocalDateTime.now())
                .build();
        transactionHistoryRepository.save(build);
    }
}
