package com.wallet.transactionservice.mapper;

import com.wallet.transactionservice.dto.BalanceDto;
import com.wallet.transactionservice.entity.Balance;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BalanceMapper extends EntityMapper<BalanceDto, Balance> {
}
