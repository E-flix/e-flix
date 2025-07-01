package com.eflix.common.payment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompletePaymentRequest {
    private String paymentId;
}