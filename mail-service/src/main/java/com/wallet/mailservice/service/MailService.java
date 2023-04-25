package com.wallet.mailservice.service;

import com.wallet.mailservice.dto.MailHistoryDto;
import com.wallet.mailservice.dto.MailVerifyDto;

public interface MailService {

    void sendEmail(MailVerifyDto mailVerifyDto);

    void sendHtmlMessage(MailVerifyDto mailVerifyDto);

}
