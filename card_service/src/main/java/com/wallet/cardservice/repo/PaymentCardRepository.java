package com.wallet.cardservice.repo;

import com.wallet.cardservice.entity.PaymentCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentCardRepository extends JpaRepository<PaymentCard, Long> {

    List<PaymentCard> findAllByUserId(Long userId);

}
