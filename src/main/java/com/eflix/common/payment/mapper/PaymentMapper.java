package com.eflix.common.payment.mapper;

import com.eflix.common.payment.Entity.PaymentEntity;
import com.eflix.common.payment.dto.SubscriptionPaymentDTO;

public interface PaymentMapper {

    public PaymentEntity findPaymentById(String paymentIdx);

    public int insertPayment(PaymentEntity paymentEntity);

    public int updatePayment(PaymentEntity paymentEntity);

    // 0705
    public void callUpdateSubscription(String spiIdx);

    public void insertPaymentLog(String spiIdx, String spiStatus, String newStatus, String changeReason);
}
