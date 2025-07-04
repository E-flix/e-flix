package com.eflix.common.payment.dto;

import lombok.Data;

// 0704

@Data
public class BillingResDTO {

    // V1
    private String billingKey;
    private String status;

    // V2
    private String code;
    private String msg;
    private String res;
}
