package com.wallet.transactionservice.mapper;

import com.wallet.transactionservice.dto.BalanceDto;
import com.wallet.transactionservice.dto.TransactionHistoryDto;
import com.wallet.transactionservice.entity.Balance;
import com.wallet.transactionservice.entity.TransactionHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionHistoryMapper extends EntityMapper<TransactionHistoryDto, TransactionHistory> {
}
