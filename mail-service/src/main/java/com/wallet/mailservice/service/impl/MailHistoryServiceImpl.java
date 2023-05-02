package com.wallet.mailservice.service.impl;

import com.wallet.mailservice.dto.MailHistoryDto;
import com.wallet.mailservice.dto.MailVerifyDto;
import com.wallet.mailservice.entity.MailHistory;
import com.wallet.mailservice.mapper.MailHistoryMapper;
import com.wallet.mailservice.repository.MailHistoryRepository;
import com.wallet.mailservice.service.MailHistoryService;
import com.wallet.mailservice.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Log4j2
public class MailHistoryServiceImpl implements MailHistoryService {

    private final MailHistoryRepository mailHistoryRepository;
    private final MailHistoryMapper mailHistoryMapper;
    private final MailService mailService;

    @Override
    public MailHistoryDto save(MailVerifyDto mailVerifyDto) {
        mailService.sendHtmlMessage(mailVerifyDto);
        MailHistoryDto build = MailHistoryDto
                .builder()
                .userEmail(mailVerifyDto.getEmail())
                .sendTime(LocalDateTime.now())
                .build();
        MailHistory save = mailHistoryRepository.save(mailHistoryMapper.toEntity(build));
        return mailHistoryMapper.toDto(save);
    }

    @Override
    public MailHistoryDto findByEmail(String email) {
        return mailHistoryRepository
                .findByUserEmail(email)
                .map(mailHistoryMapper::toDto)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public boolean isVerifyUrlExpired(String email) {
        MailHistoryDto byEmail = findByEmail(email);
        return byEmail.getSendTime().plusDays(1).isBefore(LocalDateTime.now());
    }
}