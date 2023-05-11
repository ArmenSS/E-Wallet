package com.wallet.transactionservice.dto;

import com.wallet.transactionservice.entity.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TransactionDto {

    private Long id;
    private Long userId;
    private Type type;
    private LocalDateTime datetime;
    private double amount;
    private Long toUserId;

}
