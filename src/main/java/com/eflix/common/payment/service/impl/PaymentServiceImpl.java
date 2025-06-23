package com.eflix.common.payment.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eflix.common.payment.Entity.PaymentEntity;
import com.eflix.common.payment.mapper.PaymentMapper;
import com.eflix.common.payment.service.PaymentService;
import com.eflix.erp.dto.SubscriptionPackageDetailDTO;
import com.eflix.erp.mapper.SubscriptionMapper;

/**
 * 결제 처리를 담당하는 서비스
 * 
 * <p>
 * </p>
 * 
* <h3>주요 기능</h3>
 * <ul>
 *   <li>상품 조회</li>
 * </ul>
 * 
 * @author 복성민 (bokseongmin@gmail.com)
 * @version 1.0
 * @since 2025-06-19
 * 
 * @see
 * 
 * @changelog
 * <ul>
 *   <li>2025-06-19: 최초 생성 (복성민)</li>
 *   <li>2025-06-20: 결제 코드 추가 (복성민)</li>
 * </ul>
 */

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private SubscriptionMapper subscriptionMapper;

    @Override
    public PaymentEntity getPaymentById(String paymentIdx) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'getPaymentEntity'");

        return paymentMapper.findPaymentById(paymentIdx);
    }

    @Override
    public int insertPayment(PaymentEntity paymentEntity) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'insertPayment'");

        return paymentMapper.insertPayment(paymentEntity);
    }

    @Override
    public int updatePayment(PaymentEntity paymentEntity) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'updatePayment'");

        return paymentMapper.updatePayment(paymentEntity);
    }

    @Override
    @Transactional
    public int insertSubscription(SubscriptionPackageDetailDTO subscriptionPackageDetailDTO) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'insertSubscriptionPackageDetail'");
        // return subscriptionMapper.insertSubscriptionPackageDetail(subscriptionPackageDetailDTO);
        return 0;
    }
}
