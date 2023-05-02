package com.wallet.userservice.feign;

import com.wallet.userservice.dto.MailVerifyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "mail-service",url = "http://localhost:8080/mail")
public interface MailClient {

    @PostMapping("/send-mail")
    void sendMail(MailVerifyDto mailVerifyDto);

    @GetMapping("/is-expired/{email}")
    boolean isVerifyUrlExpired(@PathVariable ("email") String email);

}
