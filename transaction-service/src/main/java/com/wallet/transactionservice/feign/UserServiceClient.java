package com.wallet.transactionservice.feign;

import com.wallet.transactionservice.dto.PaymentCardDto;
import com.wallet.userservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "user-service", url = "http://localhost:8081/user")
public interface UserServiceClient {

    @GetMapping("/byId/{id}")
    UserDto findById(@PathVariable("id") Long id);

}
