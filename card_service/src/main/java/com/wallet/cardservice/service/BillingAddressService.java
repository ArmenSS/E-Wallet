package com.wallet.cardservice.service;

import com.wallet.cardservice.dto.BillingAddressDto;
import com.wallet.cardservice.entity.BillingAddress;
import com.wallet.userservice.entity.UserEntity;

public interface BillingAddressService {

    void save(UserEntity userEntity, BillingAddress billingAddress);

    BillingAddressDto findById(Long id);

    void delete(Long id);

    BillingAddressDto update(BillingAddressDto billingAddressDto);

    BillingAddressDto findByUserId(Long userId);


}
