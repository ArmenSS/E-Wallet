package com.wallet.cardservice.mapper;

import com.wallet.cardservice.dto.PaymentCardDto;
import com.wallet.cardservice.entity.PaymentCard;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentCardMapper extends EntityMapper<PaymentCardDto, PaymentCard> {
}
