package com.wallet.cardservice.service;

import com.wallet.cardservice.dto.PaymentCardDto;
import com.wallet.cardservice.entity.PaymentCard;
import com.wallet.userservice.entity.UserEntity;

import java.util.List;

public interface PaymentCardService {

    void addPaymentCard(UserEntity userEntity, PaymentCard paymentCard);

    PaymentCardDto findById(Long id);

    void delete(Long id);

    PaymentCardDto update(PaymentCardDto paymentCardDto);

    List<PaymentCardDto> findByUserId(Long userId);

    List<PaymentCardDto> findAll(UserEntity userEntity);

}
