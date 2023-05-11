package com.wallet.transactionservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PaymentCardDto {

    private Long id;
    private Long userId;
    @Size(min = 16, max = 16, message = "name must be 16 digits long")
    private String cardNumber;
    private String nameAndSurname;
//    @JsonFormat(pattern="yyyy-MM-dd")
    private String expirationDate;
    private String cvv;

}
