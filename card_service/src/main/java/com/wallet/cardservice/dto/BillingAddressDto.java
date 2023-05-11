package com.wallet.cardservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BillingAddressDto {

    private Long id;
    private String country;
    private String province;
    private String city;
    private String addressLineOne;
    private String addressLineTwo;
    private int zipCode;
    private Long userId;

}
