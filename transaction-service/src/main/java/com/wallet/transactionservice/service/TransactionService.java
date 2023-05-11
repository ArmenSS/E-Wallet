package com.wallet.transactionservice.service;

import com.wallet.transactionservice.dto.TransactionDto;
import com.wallet.transactionservice.entity.Transaction;
import com.wallet.userservice.entity.UserEntity;

import java.util.List;

public interface TransactionService {

    void addTransaction(UserEntity userEntity, Transaction transaction);

    TransactionDto findById(Long id);

    void delete(Long id);

    TransactionDto update(TransactionDto transactionDto);

    List<TransactionDto> findAll(UserEntity userEntity);

    void deposit(UserEntity userEntity, Long cardId, double amount);

    void transfer(UserEntity userEntity, Long toUser, double amount);

    void withdraw(UserEntity userEntity, double amount);

    List<TransactionDto> seeMyHistory(UserEntity userEntity);


}
