package com.wallet.transactionservice;

import com.wallet.transactionservice.dto.BalanceDto;
import com.wallet.transactionservice.dto.PaymentCardDto;
import com.wallet.transactionservice.dto.TransactionDto;
import com.wallet.transactionservice.entity.Transaction;
import com.wallet.transactionservice.exception.MoneyIsNotEnoughException;
import com.wallet.transactionservice.feign.CardServiceClient;
import com.wallet.transactionservice.feign.UserServiceClient;
import com.wallet.transactionservice.mapper.TransactionMapper;
import com.wallet.transactionservice.repo.TransactionRepository;
import com.wallet.transactionservice.service.BalanceService;
import com.wallet.transactionservice.service.impl.TransactionServiceImpl;
import com.wallet.userservice.dto.UserDto;
import com.wallet.userservice.entity.UserEntity;
import com.wallet.userservice.entity.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TransactionServiceApplicationTests {
    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private CardServiceClient cardServiceClient;

    @Mock
    private BalanceService balanceService;

    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    public void testAddTransaction() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        transactionService.addTransaction(userEntity, transaction);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    public void testFindAllAdminUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserRole(UserRole.ADMIN);
        List<Transaction> transactions = Arrays.asList(new Transaction(), new Transaction());
        when(transactionRepository.findAll()).thenReturn(transactions);
        when(transactionMapper.toDto(anyList())).thenReturn(Arrays.asList(new TransactionDto(), new TransactionDto()));
        List<TransactionDto> result = transactionService.findAll(userEntity);
        assertEquals(2, result.size());
        verify(transactionRepository, times(1)).findAll();
        verify(transactionMapper, times(1)).toDto(transactions);
    }

    @Test
    public void testFindAllNormalUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUserRole(UserRole.USER);
        List<Transaction> transactions = Arrays.asList(new Transaction(), new Transaction());
        when(transactionRepository.findAllByUserId(anyLong())).thenReturn(transactions);
        when(transactionMapper.toDto(anyList())).thenReturn(Arrays.asList(new TransactionDto(), new TransactionDto()));
        List<TransactionDto> result = transactionService.findAll(userEntity);
        assertEquals(2, result.size());
        verify(transactionRepository, times(1)).findAllByUserId(userEntity.getId());
        verify(transactionMapper, times(1)).toDto(transactions);
    }

    @Test
    public void testFindById() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setId(1L);
        when(transactionRepository.findById(anyLong())).thenReturn(Optional.of(transaction));
        when(transactionMapper.toDto(transaction)).thenReturn(new TransactionDto());
        when(transactionMapper.toDto(any(Transaction.class))).thenReturn(transactionDto);
        TransactionDto result = transactionService.findById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(transactionRepository, times(1)).findById(1L);
        verify(transactionMapper, times(1)).toDto(transaction);
    }

    @Test
    public void testDelete() {
        Long transactionId = 1L;
        transactionService.delete(transactionId);
        verify(transactionRepository, times(1)).deleteById(transactionId);
    }

    @Test
    public void testUpdate() {
        Long transactionId = 1L;
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setId(transactionId);
        Transaction transaction = new Transaction();
        transaction.setId(transactionId);
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(transactionMapper.toDto(transaction)).thenReturn(transactionDto);
        when(transactionMapper.toEntity(transactionDto)).thenReturn(transaction);
        TransactionDto result = transactionService.update(transactionDto);
        assertNotNull(result);
        assertEquals(transactionId, result.getId());
        verify(transactionRepository, times(1)).findById(transactionId);
        verify(transactionRepository, times(1)).save(transaction);
        verify(transactionMapper, times(1)).toDto(transaction);
    }

    @Test
    public void testDeposit() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        Long cardId = 1L;
        double amount = 100.0;
        PaymentCardDto paymentCardDto = new PaymentCardDto();
        paymentCardDto.setId(1L);
        List<PaymentCardDto> cards = Arrays.asList(paymentCardDto);
        BalanceDto balanceDto = new BalanceDto();
        balanceDto.setAmounts(200.0);
        when(cardServiceClient.getPaymentCardsByUserId(userEntity.getId())).thenReturn(cards);
        when(balanceService.findByUserId(userEntity.getId())).thenReturn(balanceDto);
        when(balanceService.update(balanceDto)).thenReturn(balanceDto);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());
        transactionService.deposit(userEntity, cardId, amount);
        verify(cardServiceClient, times(1)).getPaymentCardsByUserId(userEntity.getId());
        verify(balanceService, times(1)).findByUserId(userEntity.getId());
        verify(balanceService, times(1)).update(balanceDto);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void testWithdrawEnoughBalance() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        double amount = 50.0;
        BalanceDto balanceDto = new BalanceDto();
        balanceDto.setAmounts(100.0);
        when(balanceService.findByUserId(userEntity.getId())).thenReturn(balanceDto);
        when(balanceService.update(balanceDto)).thenReturn(balanceDto);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());
        transactionService.withdraw(userEntity, amount);
        verify(balanceService, times(1)).findByUserId(userEntity.getId());
        verify(balanceService, times(1)).update(balanceDto);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void testWithdrawInsufficientBalance() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        double amount = 150.0;
        BalanceDto balanceDto = new BalanceDto();
        balanceDto.setAmounts(100.0);
        when(balanceService.findByUserId(userEntity.getId())).thenReturn(balanceDto);
        assertThrows(
                MoneyIsNotEnoughException.class,
                () -> transactionService.withdraw(userEntity, amount));
    }

    @Test
    public void testSeeMyHistory() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        List<Transaction> transactions = Arrays.asList(new Transaction(), new Transaction());
        when(transactionRepository.findAllByUserId(userEntity.getId())).thenReturn(transactions);
        when(transactionMapper.toDto(anyList())).thenReturn(Arrays.asList(new TransactionDto(), new TransactionDto()));
        List<TransactionDto> result = transactionService.seeMyHistory(userEntity);
        assertEquals(2, result.size());
        verify(transactionRepository, times(1)).findAllByUserId(userEntity.getId());
        verify(transactionMapper, times(1)).toDto(transactions);
    }

    @Test
    public void testTransferEnoughBalance() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        Long toUser = 2L;
        double amount = 50.0;
        BalanceDto fromBalance = new BalanceDto();
        fromBalance.setAmounts(100.0);
        BalanceDto toBalance = new BalanceDto();
        toBalance.setAmounts(200.0);
        UserDto transferTo = new UserDto();
        transferTo.setId(toUser);
        when(balanceService.findByUserId(userEntity.getId())).thenReturn(fromBalance);
        when(balanceService.findByUserId(toUser)).thenReturn(toBalance);
        when(balanceService.update(fromBalance)).thenReturn(fromBalance);
        when(balanceService.update(toBalance)).thenReturn(toBalance);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());
        when(userServiceClient.findById(toUser)).thenReturn(transferTo);
        transactionService.transfer(userEntity, toUser, amount);
        verify(balanceService, times(1)).findByUserId(userEntity.getId());
        verify(balanceService, times(1)).findByUserId(toUser);
        verify(balanceService, times(1)).update(fromBalance);
        verify(balanceService, times(1)).update(toBalance);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(userServiceClient, times(1)).findById(toUser);
    }

    @Test
    public void testTransferInsufficientBalance() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        Long toUser = 2L;
        double amount = 150.0;
        BalanceDto fromBalance = new BalanceDto();
        fromBalance.setAmounts(100.0);
        UserDto transferTo = new UserDto();
        transferTo.setId(toUser);
        when(balanceService.findByUserId(userEntity.getId())).thenReturn(fromBalance);
        when(userServiceClient.findById(toUser)).thenReturn(transferTo);
        assertThrows(
                MoneyIsNotEnoughException.class,
                () -> transactionService.transfer(userEntity, toUser, amount));
    }

}
