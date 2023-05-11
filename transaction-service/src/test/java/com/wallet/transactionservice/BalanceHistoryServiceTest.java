package com.wallet.transactionservice;

import com.wallet.transactionservice.dto.BalanceHistoryDto;
import com.wallet.transactionservice.entity.BalanceHistory;
import com.wallet.transactionservice.mapper.BalanceHistoryMapper;
import com.wallet.transactionservice.repo.BalanceHistoryRepository;
import com.wallet.transactionservice.repo.BalanceRepository;
import com.wallet.transactionservice.service.impl.BalanceHistoryServiceImpl;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BalanceHistoryServiceTest {

    @Mock
    private BalanceHistoryRepository balanceHistoryRepository;

    @Mock
    private BalanceRepository balanceRepository;

    @Mock
    private BalanceHistoryMapper balanceHistoryMapper;

    @InjectMocks
    private BalanceHistoryServiceImpl balanceHistoryService;

    @Test
    public void testSave() {
        double totalBalance = 1000.0;
        when(balanceRepository.findSumAmounts()).thenReturn(totalBalance);
        balanceHistoryService.save();
        ArgumentCaptor<BalanceHistory> balanceHistoryCaptor = ArgumentCaptor.forClass(BalanceHistory.class);
        verify(balanceHistoryRepository, times(1)).save(balanceHistoryCaptor.capture());
        BalanceHistory savedBalanceHistory = balanceHistoryCaptor.getValue();
        assertNotNull(savedBalanceHistory);
        assertEquals(totalBalance, savedBalanceHistory.getAmounts(), 0.01);
        verify(balanceRepository, times(1)).findSumAmounts();
    }

    @Test
    public void testFindTotalBalancesForEveryHourUserRoleAdmin() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserRole(UserRole.ADMIN);
        List<BalanceHistory> balanceHistories = Arrays.asList(
                BalanceHistory.builder().amounts(1000.0).localDateTime(LocalDateTime.now()).build(),
                BalanceHistory.builder().amounts(2000.0).localDateTime(LocalDateTime.now()).build()
        );
        when(balanceHistoryRepository.findLastHistory()).thenReturn(balanceHistories);
        when(balanceHistoryMapper.toDto(balanceHistories)).thenReturn(Arrays.asList(
                BalanceHistoryDto.builder().amounts(1000.0).localDateTime(LocalDateTime.now()).build(),
                BalanceHistoryDto.builder().amounts(2000.0).localDateTime(LocalDateTime.now()).build()
        ));
        List<BalanceHistoryDto> result = balanceHistoryService.findTotalBalancesForEveryHour(userEntity);
        assertEquals(2, result.size());
        assertEquals(1000.0, result.get(0).getAmounts(), 0.01);
        assertEquals(2000.0, result.get(1).getAmounts(), 0.01);
        verify(balanceHistoryRepository, times(1)).findLastHistory();
        verify(balanceHistoryMapper, times(1)).toDto(balanceHistories);
    }


}
