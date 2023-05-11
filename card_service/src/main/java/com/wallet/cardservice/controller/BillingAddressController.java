package com.wallet.cardservice.controller;

import com.wallet.cardservice.dto.BillingAddressDto;
import com.wallet.cardservice.entity.BillingAddress;
import com.wallet.cardservice.service.BillingAddressService;
import com.wallet.userservice.entity.UserEntity;
import com.wallet.userservice.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/billing-addresses")
@RequiredArgsConstructor
public class BillingAddressController {

    private final BillingAddressService billingAddressService;

    @PostMapping
    public ResponseEntity<?> addBillingAddress(
            @AuthenticationPrincipal CurrentUser currentUser,
            @RequestBody BillingAddress billingAddress
    ) {
        billingAddressService.save(currentUser.getUser(), billingAddress);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BillingAddressDto> updateBillingAddress(
            @PathVariable Long id,
            @RequestBody BillingAddressDto billingAddressDto
    ) {
        billingAddressDto.setId(id);
        BillingAddressDto updatedBillingAddress = billingAddressService.update(billingAddressDto);
        return new ResponseEntity<>(updatedBillingAddress, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillingAddressDto> getBillingAddressById(@PathVariable Long id) {
        BillingAddressDto billingAddressDto = billingAddressService.findById(id);
        return new ResponseEntity<>(billingAddressDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBillingAddressById(@PathVariable Long id) {
        billingAddressService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<BillingAddressDto> getBillingAddressByUserId(@PathVariable Long userId) {
        BillingAddressDto billingAddressDto = billingAddressService.findByUserId(userId);
        return new ResponseEntity<>(billingAddressDto, HttpStatus.OK);
    }
}