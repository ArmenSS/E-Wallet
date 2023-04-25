package com.wallet.mailservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MailHistoryDto {

    private Long id;
    private String userEmail;
    private LocalDateTime sendTime;

}
