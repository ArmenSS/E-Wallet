package com.wallet.cardservice.mapper;

import com.wallet.cardservice.dto.BillingAddressDto;
import com.wallet.cardservice.entity.BillingAddress;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BillingAddressMapper extends EntityMapper<BillingAddressDto, BillingAddress> {
}
