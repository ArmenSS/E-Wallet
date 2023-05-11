package com.wallet.transactionservice;


import com.wallet.transactionservice.dto.BalanceDto;
import com.wallet.transactionservice.entity.Balance;
import com.wallet.transactionservice.mapper.BalanceMapper;
import com.wallet.transactionservice.repo.BalanceRepository;
import com.wallet.transactionservice.service.impl.BalanceServiceImpl;
import com.wallet.userservice.entity.UserEntity;
import com.wallet.userservice.entity.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BalanceServiceImplTest {

    @Mock
    private BalanceRepository balanceRepository;

    @Mock
    private BalanceMapper balanceMapper;

    @InjectMocks
    private BalanceServiceImpl balanceService;

    private UserEntity userEntity;

    private Balance balance;

    private BalanceDto balanceDto;

    @BeforeEach
    public void setUp() {
        userEntity = new UserEntity();
        userEntity.setId(1L);

        balance = new Balance();
        balance.setAmounts(150.5);

        balanceDto = new BalanceDto();
        balanceDto.setId(1L);
        balanceDto.setAmounts(150.5);

        when(balanceMapper.toDto(balance)).thenReturn(balanceDto);
        when(balanceMapper.toEntity(balanceDto)).thenReturn(balance);
    }

    @Test
    public void testFindAll_UserRoleUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserRole(UserRole.USER);
        userEntity.setId(1L);
        List<Balance> balances = Arrays.asList(
                Balance.builder().userId(1L).amounts(100.0).build(),
                Balance.builder().userId(1L).amounts(200.0).build()
        );
        when(balanceRepository.findAllByUserId(userEntity.getId())).thenReturn(balances);
        when(balanceMapper.toDto(balances)).thenReturn(Arrays.asList(
                BalanceDto.builder().userId(1L).amounts(100.0).build(),
                BalanceDto.builder().userId(1L).amounts(200.0).build()
        ));
        List<BalanceDto> result = balanceService.findAll(userEntity);
        assertEquals(2, result.size());
        assertEquals(100.0, result.get(0).getAmounts(), 0.001);
        assertEquals(200.0, result.get(1).getAmounts(), 0.001);
        verify(balanceRepository, times(1)).findAllByUserId(userEntity.getId());
        verify(balanceMapper, times(1)).toDto(balances);
    }

    @Test
    public void testFindAll_UserRoleAdmin() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserRole(UserRole.ADMIN);
        List<Balance> balances = Arrays.asList(
                Balance.builder().userId(1L).amounts(100.0).build(),
                Balance.builder().userId(2L).amounts(200.0).build()
        );
        when(balanceRepository.findAll()).thenReturn(balances);
        when(balanceMapper.toDto(balances)).thenReturn(Arrays.asList(
                BalanceDto.builder().userId(1L).amounts(100.0).build(),
                BalanceDto.builder().userId(2L).amounts(200.0).build()
        ));
        List<BalanceDto> result = balanceService.findAll(userEntity);
        assertEquals(2, result.size());
        assertEquals(100.0, result.get(0).getAmounts(), 0.001);
        assertEquals(200.0, result.get(1).getAmounts(), 0.001);
        verify(balanceRepository, times(1)).findAll();
        verify(balanceMapper, times(1)).toDto(balances);
    }

    @Test
    public void testAddBalance() {
        Long userId = 1L;
        Balance balance = Balance.builder()
                .userId(userId)
                .amounts(0)
                .build();
        when(balanceRepository.save(balance)).thenReturn(balance);
        balanceService.addBalance(userId);
        verify(balanceRepository, times(1)).save(balance);
    }


    @Test
    public void testFindById() {
        when(balanceRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(balance));
        BalanceDto found = balanceService.findById(1L);
        assertEquals(balanceDto, found);
    }

    @Test
    public void testDelete() {
        balanceService.delete(1L);
        verify(balanceRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testUpdate() {
        balance.setAmounts(220.0);
        when(balanceRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(balance));
        when(balanceRepository.save(Mockito.any(Balance.class))).thenReturn(balance);
        BalanceDto updated = balanceService.update(balanceDto);
        assertEquals(balanceDto, updated);
    }

}