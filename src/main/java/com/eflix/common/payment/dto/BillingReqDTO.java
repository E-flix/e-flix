package com.eflix.common.payment.dto;

import lombok.Data;

// 0704

@Data
public class BillingReqDTO {

    // V1
    private String impUid;
    private String customerUid;

    // V2
    private String number;
    private String expiryYear;
    private String expiryMonth;
    private String birthOrBusinessRegistrationNumber;
    private String passwordTwoDigits;
    private String customerId;
}