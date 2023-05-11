package com.wallet.transactionservice.service.impl;

import com.wallet.transactionservice.dto.BalanceHistoryDto;
import com.wallet.transactionservice.entity.BalanceHistory;
import com.wallet.transactionservice.exception.AccessDeniedException;
import com.wallet.transactionservice.mapper.BalanceHistoryMapper;
import com.wallet.transactionservice.repo.BalanceHistoryRepository;
import com.wallet.transactionservice.repo.BalanceRepository;
import com.wallet.transactionservice.service.BalanceHistoryService;
import com.wallet.userservice.entity.UserEntity;
import com.wallet.userservice.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BalanceHistoryServiceImpl implements BalanceHistoryService {

    private final BalanceHistoryRepository balanceHistoryRepository;
    private final BalanceRepository balanceRepository;
    private final BalanceHistoryMapper balanceHistoryMapper;

    @Override
    @Scheduled(cron = "0 15/15 * * * *")
    public void save() {
        System.out.println("barev "+LocalDateTime.now());
        double totalBalance = balanceRepository.findSumAmounts();
        BalanceHistory balanceHistory = BalanceHistory.builder()
                .amounts(totalBalance)
                .localDateTime(LocalDateTime.now())
                .build();
        balanceHistoryRepository.save(balanceHistory);
    }

    @Override
    public List<BalanceHistoryDto> findTotalBalancesForEveryHour(UserEntity userEntity) {
        if (userEntity.getUserRole().equals(UserRole.ADMIN)) {
            return balanceHistoryMapper.toDto(balanceHistoryRepository.findLastHistory());
        }
        throw new AccessDeniedException();
    }
}
