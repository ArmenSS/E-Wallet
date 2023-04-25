package com.wallet.mailservice.controller;

import com.wallet.mailservice.dto.MailHistoryDto;
import com.wallet.mailservice.dto.MailVerifyDto;
import com.wallet.mailservice.service.MailHistoryService;
import com.wallet.mailservice.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mail")
public class MailController {

    private final MailHistoryService mailHistoryService;
    private final MailService mailService;

    /**
     * @param mailVerifyDto
     * @return ResponseEntity<MailVerifyDto> This controller accept MailVerifyDto ans save it
     * in DB after that return MailVerifyDto
     */
    @PostMapping("/save")
    public ResponseEntity<MailHistoryDto> save(@RequestBody MailVerifyDto mailVerifyDto) {
        return ResponseEntity.ok(mailHistoryService.save(mailVerifyDto));
    }

    /**
     * @param email
     * @return MailHistoryDto This controller find classroom byEmail, when email exist return
     * MailHistoryDto
     */
    @GetMapping("/byEmail/{email}")
    public MailHistoryDto findByEmail(@PathVariable("email") String email) {
        return mailHistoryService.findByEmail(email);
    }

    /**
     * @param email param will be validated
     * @return boolean
     */
    @GetMapping("/is-expired/{email}")
    public boolean isVerifyUrlExpired(@PathVariable("email") String email) {
        return mailHistoryService.isVerifyUrlExpired(email);
    }

    @PostMapping("/send-mail")
    public void sendMail(@RequestBody MailVerifyDto mailVerifyDto) {
        mailService.sendHtmlMessage(mailVerifyDto);
        mailHistoryService.save(mailVerifyDto);
    }

}
