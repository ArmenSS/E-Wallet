package com.wallet.transactionservice.service;

import com.wallet.transactionservice.dto.TransactionHistoryDto;
import com.wallet.userservice.entity.UserEntity;

import java.util.List;

public interface TransactionHistoryService {

    List<TransactionHistoryDto> findTransactionsCountForLastHours(UserEntity userEntity);

    void save();

}
