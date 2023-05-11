package com.wallet.transactionservice;

import com.wallet.transactionservice.dto.TransactionHistoryDto;
import com.wallet.transactionservice.entity.TransactionHistory;
import com.wallet.transactionservice.mapper.TransactionHistoryMapper;
import com.wallet.transactionservice.repo.TransactionHistoryRepository;
import com.wallet.transactionservice.repo.TransactionRepository;
import com.wallet.transactionservice.service.impl.TransactionHistoryServiceImpl;
import com.wallet.userservice.entity.UserEntity;
import com.wallet.userservice.entity.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TransactionHistoryServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionHistoryRepository transactionHistoryRepository;

    @Mock
    private TransactionHistoryMapper transactionHistoryMapper;

    @InjectMocks
    private TransactionHistoryServiceImpl transactionHistoryService;

    @Test
    public void testFindTransactionsCountForLastHoursUserRoleAdmin() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserRole(UserRole.ADMIN);
        List<TransactionHistory> transactionHistories = Arrays.asList(
                TransactionHistory.builder().count(10).datetime(LocalDateTime.now()).build(),
                TransactionHistory.builder().count(15).datetime(LocalDateTime.now()).build()
        );
        when(transactionHistoryRepository.findLastHistory()).thenReturn(transactionHistories);
        when(transactionHistoryMapper.toDto(transactionHistories)).thenReturn(Arrays.asList(
                TransactionHistoryDto.builder().count(10).datetime(LocalDateTime.now()).build(),
                TransactionHistoryDto.builder().count(15).datetime(LocalDateTime.now()).build()
        ));
        List<TransactionHistoryDto> result = transactionHistoryService.findTransactionsCountForLastHours(userEntity);
        assertEquals(2, result.size());
        assertEquals(10, result.get(0).getCount());
        assertEquals(15, result.get(1).getCount());
        verify(transactionHistoryRepository, times(1)).findLastHistory();
        verify(transactionHistoryMapper, times(1)).toDto(transactionHistories);
    }

    @Test
    public void testFindTransactionsCountForLastHoursUserRoleUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserRole(UserRole.USER);
        List<TransactionHistoryDto> result = transactionHistoryService.findTransactionsCountForLastHours(userEntity);
        assertNull(result);
    }

    @Test
    public void testSave() {
        int count = 5;
        when(transactionRepository.findTransactionsBefore15Minutes()).thenReturn(count);
        transactionHistoryService.save();
        ArgumentCaptor<TransactionHistory> transactionHistoryCaptor = ArgumentCaptor.forClass(TransactionHistory.class);
        verify(transactionHistoryRepository, times(1)).save(transactionHistoryCaptor.capture());
        TransactionHistory savedTransactionHistory = transactionHistoryCaptor.getValue();
        assertNotNull(savedTransactionHistory);
        assertEquals(count, savedTransactionHistory.getCount());
        verify(transactionRepository, times(1)).findTransactionsBefore15Minutes();
    }
}
