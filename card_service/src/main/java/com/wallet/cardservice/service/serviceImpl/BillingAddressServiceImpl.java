package com.wallet.cardservice.service.serviceImpl;

import com.wallet.cardservice.dto.BillingAddressDto;
import com.wallet.cardservice.entity.BillingAddress;
import com.wallet.cardservice.mapper.BillingAddressMapper;
import com.wallet.cardservice.repo.BillingAddressRepository;
import com.wallet.cardservice.service.BillingAddressService;
import com.wallet.userservice.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Log4j2
public class BillingAddressServiceImpl implements BillingAddressService {

    private final BillingAddressRepository billingAddressRepository;
    private final BillingAddressMapper billingAddressMapper;


    @Override
    public void save(UserEntity userEntity, BillingAddress billingAddress) {
        billingAddress.setId(userEntity.getId());
        billingAddressRepository.save(billingAddress);
    }

    @Override
    public BillingAddressDto update(BillingAddressDto billingAddressDto) {
        billingAddressRepository.findById(billingAddressDto.getId()).orElseThrow(EntityNotFoundException::new);
        BillingAddress billingAddress = billingAddressRepository.save(billingAddressMapper.toEntity(billingAddressDto));
        return billingAddressMapper.toDto(billingAddress);
    }

    @Override
    public BillingAddressDto findById(Long id) {
        BillingAddress billingAddress = billingAddressRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return billingAddressMapper.toDto(billingAddress);
    }

    @Override
    public void delete(Long id) {
        billingAddressRepository.deleteById(id);
    }

    @Override
    public BillingAddressDto findByUserId(Long userId) {
        return billingAddressMapper.toDto(billingAddressRepository.findByUserId(userId));
    }
}
