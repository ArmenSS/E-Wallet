package com.wallet.cardservice.service.serviceImpl;

import com.wallet.cardservice.dto.PaymentCardDto;
import com.wallet.cardservice.entity.PaymentCard;
import com.wallet.cardservice.exception.CardDateExpiredException;
import com.wallet.cardservice.mapper.PaymentCardMapper;
import com.wallet.cardservice.repo.PaymentCardRepository;
import com.wallet.cardservice.service.PaymentCardService;
import com.wallet.userservice.entity.UserEntity;
import com.wallet.userservice.entity.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class PaymentCardServiceImpl implements PaymentCardService {

    private final PaymentCardRepository paymentCardRepository;
    private final PaymentCardMapper paymentCardMapper;

    private static final String DATE_REGEX =
            "/^(0[1-9]|1[0-2])\\/?([0-9]{4}|[0-9]{2})$/";

    @Override
    public void addPaymentCard(UserEntity userEntity, PaymentCard paymentCard) throws IllegalArgumentException {
        if (paymentCard.getExpirationDate().matches(DATE_REGEX)) {
            throw new CardDateExpiredException();
        } else if (!isValidExpirationDate(paymentCard.getExpirationDate())) {
            throw new CardDateExpiredException();
        } else {
            paymentCard.setUserId(userEntity.getId());
            paymentCardRepository.save(paymentCard);
        }
    }

    @Override
    public List<PaymentCardDto> findAll(UserEntity userEntity) {
        if (userEntity.getUserRole().equals(UserRole.USER)) {
            return paymentCardMapper
                    .toDto(paymentCardRepository.findAllByUserId(userEntity.getId()));
        } else {
            return paymentCardMapper.toDto(paymentCardRepository.findAll());
        }
    }

    @Override
    public PaymentCardDto findById(Long id) {
        PaymentCard paymentCard = paymentCardRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return paymentCardMapper.toDto(paymentCard);
    }

    @Override
    public void delete(Long id) {
        paymentCardRepository.deleteById(id);
    }

    @Override
    public PaymentCardDto update(PaymentCardDto paymentCardDto) {
        paymentCardRepository.findById(paymentCardDto.getId()).orElseThrow(EntityNotFoundException::new);
        PaymentCard paymentCard = paymentCardRepository.save(paymentCardMapper.toEntity(paymentCardDto));
        return paymentCardMapper.toDto(paymentCard);
    }

    @Override
    public List<PaymentCardDto> findByUserId(Long userId) {
        return paymentCardMapper.toDto(paymentCardRepository.findAllByUserId(userId));
    }

    private boolean isValidExpirationDate(String expirationDate) {
        String[] parts = expirationDate.split("/");
        if (parts.length != 2) {
            return false;
        }
        int month, year;
        try {
            month = Integer.parseInt(parts[0]);
            year = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            return false;
        }
        LocalDate now = LocalDate.now();
        return (year > now.getYear() || (year == now.getYear() && month >= now.getMonthValue()));
    }

}
