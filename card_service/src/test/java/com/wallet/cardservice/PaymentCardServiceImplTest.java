package com.wallet.cardservice;


import com.wallet.cardservice.dto.PaymentCardDto;
import com.wallet.cardservice.entity.PaymentCard;
import com.wallet.cardservice.exception.CardDateExpiredException;
import com.wallet.cardservice.mapper.PaymentCardMapper;
import com.wallet.cardservice.repo.PaymentCardRepository;
import com.wallet.cardservice.service.serviceImpl.PaymentCardServiceImpl;
import com.wallet.userservice.entity.UserEntity;
import com.wallet.userservice.entity.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PaymentCardServiceImplTest {

    @Mock
    private PaymentCardRepository paymentCardRepository;

    @Mock
    private PaymentCardMapper paymentCardMapper;

    @InjectMocks
    private PaymentCardServiceImpl paymentCardService;

    private PaymentCard paymentCard;

    private PaymentCardDto paymentCardDto;

    @BeforeEach
    public void setUp() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        paymentCard = new PaymentCard();
        paymentCardDto = new PaymentCardDto();
        paymentCardDto.setId(1L);
        paymentCardService = new PaymentCardServiceImpl(paymentCardRepository, paymentCardMapper);
        when(paymentCardMapper.toDto(paymentCard)).thenReturn(paymentCardDto);
        when(paymentCardMapper.toEntity(paymentCardDto)).thenReturn(paymentCard);
    }

    @Test
    public void testAddPaymentCard_validCard_success() {
        UserEntity userEntity = new UserEntity();
        PaymentCard paymentCard = new PaymentCard();
        paymentCard.setExpirationDate("12/2025");

        when(paymentCardRepository.save(any(PaymentCard.class))).thenReturn(paymentCard);

        assertDoesNotThrow(() -> paymentCardService.addPaymentCard(userEntity, paymentCard));

        verify(paymentCardRepository, times(1)).save(paymentCard);
    }

    @Test
    public void testAddPaymentCard_expiredCard_exceptionThrown() {
        UserEntity userEntity = new UserEntity();
        PaymentCard paymentCard = new PaymentCard();
        paymentCard.setExpirationDate("12/2020");

        assertThrows(CardDateExpiredException.class, () -> paymentCardService.addPaymentCard(userEntity, paymentCard));

        verify(paymentCardRepository, never()).save(paymentCard);
    }

    @Test
    public void testFindAll_userRoleUser_returnDtoList() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserRole(UserRole.USER);

        List<PaymentCard> paymentCards = new ArrayList<>();
        paymentCards.add(new PaymentCard());
        paymentCards.add(new PaymentCard());

        when(paymentCardRepository.findAllByUserId(userEntity.getId())).thenReturn(paymentCards);
        when(paymentCardMapper.toDto(paymentCards)).thenReturn(new ArrayList<>());

        List<PaymentCardDto> result = paymentCardService.findAll(userEntity);

        assertNotNull(result);
        assertEquals(0, result.size());

        verify(paymentCardRepository, times(1)).findAllByUserId(userEntity.getId());
        verify(paymentCardMapper, times(1)).toDto(paymentCards);
    }

    @Test
    public void testFindAll_userRoleAdmin_returnAllDtoList() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserRole(UserRole.ADMIN);

        List<PaymentCard> paymentCards = new ArrayList<>();
        paymentCards.add(new PaymentCard());
        paymentCards.add(new PaymentCard());

        when(paymentCardRepository.findAll()).thenReturn(paymentCards);
        when(paymentCardMapper.toDto(paymentCards)).thenReturn(new ArrayList<>());

        List<PaymentCardDto> result = paymentCardService.findAll(userEntity);

        assertNotNull(result);
        assertEquals(0, result.size());

        verify(paymentCardRepository, times(1)).findAll();
        verify(paymentCardMapper, times(1)).toDto(paymentCards);
    }

    @Test
    public void testFindById() {
        when(paymentCardRepository.findById(anyLong())).thenReturn(Optional.of(paymentCard));
        PaymentCardDto found = paymentCardService.findById(1L);
        assertEquals(paymentCardDto, found);
    }

    @Test
    public void testDelete() {
        paymentCardService.delete(1L);
        verify(paymentCardRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testUpdate() {
        paymentCardDto.setCardNumber("1234567812345679");
        when(paymentCardRepository.findById(anyLong())).thenReturn(Optional.of(paymentCard));
        when(paymentCardRepository.save(any(PaymentCard.class))).thenReturn(paymentCard);
        PaymentCardDto updated = paymentCardService.update(paymentCardDto);
        assertEquals(paymentCardDto, updated);
    }

    @Test
    public void testFindByUserId() {
        List<PaymentCard> paymentCards = new ArrayList<>();
        paymentCards.add(paymentCard);

        when(paymentCardRepository.findAllByUserId(anyLong())).thenReturn(paymentCards);
        when(paymentCardMapper.toDto(anyList())).thenReturn(Collections.singletonList(paymentCardDto));

        List<PaymentCardDto> found = paymentCardService.findByUserId(1L);
        assertEquals(1, found.size());
        assertEquals(paymentCardDto, found.get(0));
    }
}