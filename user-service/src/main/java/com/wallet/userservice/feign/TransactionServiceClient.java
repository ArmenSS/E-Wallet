package com.wallet.userservice.feign;

import com.wallet.userservice.dto.MailVerifyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "transaction-service",url = "http://localhost:8088")
public interface TransactionServiceClient {

    @PostMapping("/balance/{userId}")
    void createBalance(@PathVariable Long userId);

}
