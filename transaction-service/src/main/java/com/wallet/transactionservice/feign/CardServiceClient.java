package com.wallet.transactionservice.feign;

import com.wallet.transactionservice.dto.PaymentCardDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "card-service", url = "http://localhost:8084/card")
public interface CardServiceClient {

    @GetMapping
    List<PaymentCardDto> getPaymentCardsByUserId(@RequestParam Long userId);

}
