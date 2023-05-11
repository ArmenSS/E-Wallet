package com.wallet.transactionservice.service;

import com.wallet.transactionservice.dto.BalanceHistoryDto;
import com.wallet.userservice.entity.UserEntity;

import java.util.List;

public interface BalanceHistoryService {

    List<BalanceHistoryDto> findTotalBalancesForEveryHour(UserEntity userEntity);

    void save();

}
