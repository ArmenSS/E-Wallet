package com.wallet.transactionservice.service;

import com.wallet.transactionservice.dto.BalanceDto;
import com.wallet.transactionservice.entity.Balance;
import com.wallet.userservice.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface BalanceService {

    void addBalance(Long userId);

    BalanceDto findById(Long id);

    BalanceDto findByUserId(Long userId);

    void delete(Long id);

    BalanceDto update(BalanceDto balanceDto);

    List<BalanceDto> findAll(UserEntity userEntity);

    List<BalanceDto> findAllByUsersAndBalance(UserEntity userEntity, Long userId);

    Optional<Balance> seeMyBalance(UserEntity userEntity);
}
