package com.wallet.mailservice;

import com.wallet.mailservice.dto.MailHistoryDto;
import com.wallet.mailservice.dto.MailVerifyDto;
import com.wallet.mailservice.entity.MailHistory;
import com.wallet.mailservice.mapper.MailHistoryMapper;
import com.wallet.mailservice.repository.MailHistoryRepository;
import com.wallet.mailservice.service.MailService;
import com.wallet.mailservice.service.impl.MailHistoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MailHistoryServiceImplTest {

    @Mock
    private MailHistoryRepository mailHistoryRepository;

    @Mock
    private MailHistoryMapper mailHistoryMapper;

    @Mock
    private MailService mailService;

    @InjectMocks
    private MailHistoryServiceImpl mailHistoryService;

    @Test
    public void saveTest() {
        MailVerifyDto mailVerifyDto = new MailVerifyDto();
        mailVerifyDto.setEmail("test@example.com");
        MailHistoryDto mailHistoryDto = MailHistoryDto.builder()
                .userEmail(mailVerifyDto.getEmail())
                .sendTime(LocalDateTime.now())
                .build();
        MailHistory mailHistory = new MailHistory();
        when(mailHistoryMapper.toEntity(mailHistoryDto)).thenReturn(mailHistory);
        when(mailHistoryRepository.save(mailHistory)).thenReturn(mailHistory);
        when(mailHistoryMapper.toDto(mailHistory)).thenReturn(mailHistoryDto);
        when(mailHistoryService.save(mailVerifyDto)).thenReturn(mailHistoryDto);
        MailHistoryDto savedMailHistoryDto = mailHistoryService.save(mailVerifyDto);
        assertNotNull(savedMailHistoryDto);
        assertEquals(mailVerifyDto.getEmail(), savedMailHistoryDto.getUserEmail());
        assertNotNull(savedMailHistoryDto.getSendTime());
    }

    @Test
    public void findByEmailTest() {
        String email = "test@example.com";
        MailHistory mailHistory = new MailHistory();
        when(mailHistoryRepository.findByUserEmail(email)).thenReturn(Optional.of(mailHistory));
        MailHistoryDto mailHistoryDto = MailHistoryDto.builder().build();
        when(mailHistoryMapper.toDto(mailHistory)).thenReturn(mailHistoryDto);
        MailHistoryDto foundMailHistoryDto = mailHistoryService.findByEmail(email);
        assertNotNull(foundMailHistoryDto);
        verify(mailHistoryRepository, times(1)).findByUserEmail(email);
        verify(mailHistoryMapper, times(1)).toDto(mailHistory);
    }

    @Test
    public void findByEmailNotFoundTest() {
        String email = "test@example.com";
        when(mailHistoryRepository.findByUserEmail(email)).thenReturn(Optional.empty());
        assertThrows(
                EntityNotFoundException.class,
                () -> mailHistoryService.findByEmail(email));
    }

}