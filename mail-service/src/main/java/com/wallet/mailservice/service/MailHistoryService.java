package com.wallet.mailservice.service;

import com.wallet.mailservice.dto.MailHistoryDto;
import com.wallet.mailservice.dto.MailVerifyDto;

public interface MailHistoryService {

    MailHistoryDto save(MailVerifyDto mailVerifyDto);

    MailHistoryDto findByEmail(String email);

    boolean isVerifyUrlExpired(String email);

}
