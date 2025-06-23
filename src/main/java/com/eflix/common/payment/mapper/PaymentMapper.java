package com.eflix.common.payment.mapper;

import com.eflix.common.payment.Entity.PaymentEntity;

public interface PaymentMapper {

    public PaymentEntity findPaymentById(String paymentIdx);

    public int insertPayment(PaymentEntity paymentEntity);

    public int updatePayment(PaymentEntity paymentEntity);
}
