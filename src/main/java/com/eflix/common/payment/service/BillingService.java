package com.eflix.common.payment.service;

import com.eflix.common.payment.dto.BillingReqDTO;

public interface BillingService {

    // V1
    public String getAccessToken();
    public String verifyBillingKey(String customerId);

    // V2
    public String issueBillingKey(BillingReqDTO billingReqDTO) throws Exception;
}
