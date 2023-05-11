package com.wallet.cardservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="card")
public class PaymentCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    @Size(min = 16, max = 16, message = "card number must be 16 digits long")
    private String cardNumber;
    private String nameAndSurname;
//    @JsonFormat(pattern="yyyy-MM-dd")
    private String expirationDate;
    private String cvv;

}
