package com.wallet.transactionservice.service.impl;

import com.wallet.transactionservice.dto.BalanceDto;
import com.wallet.transactionservice.dto.PaymentCardDto;
import com.wallet.transactionservice.dto.TransactionDto;
import com.wallet.transactionservice.entity.Transaction;
import com.wallet.transactionservice.entity.Type;
import com.wallet.transactionservice.exception.MoneyIsNotEnoughException;
import com.wallet.transactionservice.feign.CardServiceClient;
import com.wallet.transactionservice.feign.UserServiceClient;
import com.wallet.transactionservice.mapper.TransactionMapper;
import com.wallet.transactionservice.repo.TransactionRepository;
import com.wallet.transactionservice.service.BalanceService;
import com.wallet.transactionservice.service.TransactionService;
import com.wallet.userservice.dto.UserDto;
import com.wallet.userservice.entity.UserEntity;
import com.wallet.userservice.entity.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final CardServiceClient cardServiceClient;
    private final BalanceService balanceService;
    private final UserServiceClient userServiceClient;


    @Override
    public void addTransaction(UserEntity userEntity, Transaction transaction) {
        transaction.setUserId(userEntity.getId());
        transaction.setDatetime(LocalDateTime.now());
        transactionRepository.save(transaction);
    }


    @Override
    public List<TransactionDto> findAll(UserEntity userEntity) {
        if (userEntity.getUserRole().equals(UserRole.ADMIN)) {
            return transactionMapper.toDto(transactionRepository.findAll());
        } else {
            return transactionMapper.toDto(transactionRepository.findAllByUserId(userEntity.getId()));
        }
    }

    @Override
    public TransactionDto findById(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return transactionMapper.toDto(transaction);
    }

    @Override
    public void delete(Long id) {
        transactionRepository.deleteById(id);
    }

    @Override
    public TransactionDto update(TransactionDto transactionDto) {
        transactionRepository.findById(transactionDto.getId()).orElseThrow(EntityNotFoundException::new);
        Transaction transaction = transactionRepository.save(transactionMapper.toEntity(transactionDto));
        return transactionMapper.toDto(transaction);
    }

    @Override
    @Transactional
    public void deposit(UserEntity userEntity, Long cardId, double amount) {
        List<PaymentCardDto> cards = cardServiceClient.getPaymentCardsByUserId(userEntity.getId());
        Optional<PaymentCardDto> paymentCard = cards.stream().filter(s -> s.getId().equals(cardId)).findFirst();
        if (paymentCard.isPresent()) {
            BalanceDto byUserId = balanceService.findByUserId(userEntity.getId());
            byUserId.setAmounts(byUserId.getAmounts() + amount);
            balanceService.update(byUserId);
            transactionRepository.save(Transaction.builder()
                    .datetime(LocalDateTime.now())
                    .type(Type.DEPOSIT)
                    .amount(amount)
                    .userId(userEntity.getId()).build());
        }
    }

    @Override
    public void transfer(UserEntity userEntity, Long toUser, double amount) {
        UserDto transferTo = userServiceClient.findById(toUser);
        BalanceDto from = balanceService.findByUserId(userEntity.getId());
        if (from.getAmounts() >= amount) {
            BalanceDto to = balanceService.findByUserId(transferTo.getId());
            to.setAmounts(to.getAmounts() + amount);
            from.setAmounts(from.getAmounts() - amount);
            balanceService.update(to);
            balanceService.update(from);
            transactionRepository.save(Transaction.builder()
                    .datetime(LocalDateTime.now())
                    .type(Type.TRANSFER)
                    .amount(amount)
                    .userId(userEntity.getId())
                    .toUserId(toUser).build());
        } else {
            throw new MoneyIsNotEnoughException();
        }
    }


    @Override
    public void withdraw(UserEntity userEntity, double amount) {
        BalanceDto byUserId = balanceService.findByUserId(userEntity.getId());
        if (byUserId.getAmounts() >= amount) {
            byUserId.setAmounts(byUserId.getAmounts() - amount);
            balanceService.update(byUserId);
            transactionRepository.save(Transaction.builder()
                    .datetime(LocalDateTime.now())
                    .type(Type.WITHDRAW)
                    .amount(amount)
                    .userId(userEntity.getId()).build());
        } else {
            throw new MoneyIsNotEnoughException();
        }
    }
    @Override
    public List<TransactionDto> seeMyHistory(UserEntity userEntity) {
        return transactionMapper.toDto(transactionRepository.findAllByUserId(userEntity.getId()));
    }

}
