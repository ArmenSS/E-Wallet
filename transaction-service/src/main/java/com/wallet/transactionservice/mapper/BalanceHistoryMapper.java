package com.wallet.transactionservice.mapper;

import com.wallet.transactionservice.dto.BalanceHistoryDto;
import com.wallet.transactionservice.entity.BalanceHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BalanceHistoryMapper extends EntityMapper<BalanceHistoryDto, BalanceHistory> {
}
