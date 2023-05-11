package com.wallet.cardservice.controller;

import com.wallet.cardservice.dto.PaymentCardDto;
import com.wallet.cardservice.entity.PaymentCard;
import com.wallet.cardservice.service.PaymentCardService;
import com.wallet.userservice.entity.UserEntity;
import com.wallet.userservice.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/card")
public class PaymentCardController {

    private final PaymentCardService paymentCardService;

    @PostMapping
    public ResponseEntity<Void> addPaymentCard(@AuthenticationPrincipal CurrentUser currentUser, @RequestBody PaymentCard paymentCard) {
        paymentCardService.addPaymentCard(currentUser.getUser(), paymentCard);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentCardDto> getPaymentCardById(@PathVariable Long id) {
        PaymentCardDto paymentCardDto = paymentCardService.findById(id);
        return ResponseEntity.ok(paymentCardDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentCard(@PathVariable Long id) {
        paymentCardService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<PaymentCardDto> updatePaymentCard(@RequestBody PaymentCardDto paymentCardDto) {
        PaymentCardDto updatedPaymentCardDto = paymentCardService.update(paymentCardDto);
        return ResponseEntity.ok(updatedPaymentCardDto);
    }

    @GetMapping
    public ResponseEntity<List<PaymentCardDto>> getPaymentCardsByUserId(@RequestParam Long userId) {
        List<PaymentCardDto> paymentCardDtos = paymentCardService.findByUserId(userId);
        return ResponseEntity.ok(paymentCardDtos);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<PaymentCardDto>> findAll(@AuthenticationPrincipal CurrentUser currentUser) {
        return ResponseEntity.ok(paymentCardService.findAll(currentUser.getUser()));
    }
}
