package com.wallet.transactionservice.service.impl;

import com.wallet.transactionservice.dto.BalanceDto;
import com.wallet.transactionservice.entity.Balance;
import com.wallet.transactionservice.exception.AccessDeniedException;
import com.wallet.transactionservice.mapper.BalanceMapper;
import com.wallet.transactionservice.repo.BalanceRepository;
import com.wallet.transactionservice.service.BalanceService;
import com.wallet.userservice.entity.UserEntity;
import com.wallet.userservice.entity.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class BalanceServiceImpl implements BalanceService {

    private final BalanceRepository balanceRepository;
    private final BalanceMapper balanceMapper;


    @Override
    public List<BalanceDto> findAll(UserEntity userEntity) {
        if (userEntity.getUserRole().equals(UserRole.USER)) {
            return balanceMapper
                    .toDto(balanceRepository.findAllByUserId(userEntity.getId()));
        } else {
            return balanceMapper.toDto(balanceRepository.findAll());
        }
    }

    @Override
    public void addBalance(Long userId) {
        Balance balance = Balance.builder()
                .userId(userId)
                .amounts(0)
                .build();
        balanceRepository.save(balance);
    }

    @Override
    public List<BalanceDto> findAllByUsersAndBalance(UserEntity userEntity, Long userId) {
        if (userEntity.getUserRole().equals(UserRole.ADMIN)) {
            return balanceMapper.toDto(balanceRepository.findAllByUserId(userId));
        }
        throw new AccessDeniedException();
    }

    @Override
    public BalanceDto findById(Long id) {
        Balance balance = balanceRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return balanceMapper.toDto(balance);
    }

    @Override
    public void delete(Long id) {
        balanceRepository.deleteById(id);
    }

    @Override
    public BalanceDto update(BalanceDto balanceDto) {
        balanceRepository.findById(balanceDto.getId()).orElseThrow(EntityNotFoundException::new);
        Balance balance = balanceRepository.save(balanceMapper.toEntity(balanceDto));
        return balanceMapper.toDto(balance);
    }

    @Override
    public BalanceDto findByUserId(Long userId) {
        return balanceMapper.toDto(balanceRepository.findByUserId(userId).orElseThrow(EntityNotFoundException::new));
    }

    @Override
    public Optional<Balance> seeMyBalance(UserEntity userEntity) {
        return balanceRepository.findByUserId(userEntity.getId());
    }


}
