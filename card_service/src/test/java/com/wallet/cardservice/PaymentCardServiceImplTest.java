package com.wallet.cardservice;


import com.wallet.cardservice.dto.PaymentCardDto;
import com.wallet.cardservice.entity.PaymentCard;
import com.wallet.cardservice.exception.CardDateExpiredException;
import com.wallet.cardservice.exception.InvalidCardNumberException;
import com.wallet.cardservice.mapper.PaymentCardMapper;
import com.wallet.cardservice.repo.PaymentCardRepository;
import com.wallet.cardservice.service.serviceImpl.PaymentCardServiceImpl;
import com.wallet.userservice.entity.UserEntity;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PaymentCardServiceImplTest {

    @Mock
    private PaymentCardRepository paymentCardRepository;

    @Mock
    private PaymentCardMapper paymentCardMapper;

    @InjectMocks
    private PaymentCardServiceImpl paymentCardService;

    private UserEntity userEntity;

    private PaymentCard paymentCard;

    private PaymentCardDto paymentCardDto;

    @BeforeEach
    public void setUp() {
        userEntity = new UserEntity();
        userEntity.setId(1L);

        paymentCard = new PaymentCard();
        paymentCard.setCardNumber("4916471077890409");
        paymentCard.setExpirationDate("04/25");

        paymentCardDto = new PaymentCardDto();
        paymentCardDto.setId(1L);
        paymentCardDto.setCardNumber("4916471077890409");
        paymentCardDto.setExpirationDate("04/25");

        Mockito.when(paymentCardMapper.toDto(paymentCard)).thenReturn(paymentCardDto);
        Mockito.when(paymentCardMapper.toEntity(paymentCardDto)).thenReturn(paymentCard);
    }

    @Test
    public void testAddPaymentCard_InvalidCardNumberException() {
        PaymentCard paymentCard = new PaymentCard();
        paymentCard.setCardNumber("12345678");
        assertThrows(
                InvalidCardNumberException.class,
                () -> paymentCardService.addPaymentCard(userEntity, paymentCard));
    }

    @Test
    public void testAddPaymentCard_CardDateExpiredException() {
        PaymentCard paymentCard = new PaymentCard();
        paymentCard.setCardNumber("4916471077890409");
        paymentCard.setExpirationDate("04/21");
        assertThrows(InvalidCardNumberException.class,
                () ->
                        paymentCardService.addPaymentCard(userEntity, paymentCard));
    }


    @Test
    public void testFindById() {
        Mockito.when(paymentCardRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(paymentCard));
        PaymentCardDto found = paymentCardService.findById(1L);
        assertEquals(paymentCardDto, found);
    }

    @Test
    public void testDelete() {
        paymentCardService.delete(1L);
        Mockito.verify(paymentCardRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void testUpdate() {
        paymentCardDto.setCardNumber("1234567812345679");
        Mockito.when(paymentCardRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(paymentCard));
        Mockito.when(paymentCardRepository.save(Mockito.any(PaymentCard.class))).thenReturn(paymentCard);
        PaymentCardDto updated = paymentCardService.update(paymentCardDto);
        assertEquals(paymentCardDto, updated);
    }

    @Test
    public void testFindByUserId() {
        List<PaymentCard> paymentCards = new ArrayList<>();
        paymentCards.add(paymentCard);

        Mockito.when(paymentCardRepository.findAllByUserId(Mockito.anyLong())).thenReturn(paymentCards);
        Mockito.when(paymentCardMapper.toDto(Mockito.anyList())).thenReturn(Collections.singletonList(paymentCardDto));

        List<PaymentCardDto> found = paymentCardService.findByUserId(1L);
        assertEquals(1, found.size());
        assertEquals(paymentCardDto, found.get(0));
    }
}