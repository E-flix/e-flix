package com.eflix.common.payment.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eflix.common.payment.Entity.PaymentEntity;
import com.eflix.common.payment.dto.SubscriptionPaymentDTO;
import com.eflix.common.payment.mapper.PaymentMapper;
import com.eflix.common.payment.service.PaymentService;
import com.eflix.main.dto.SubscriptionDTO;
import com.eflix.main.dto.SubscriptionPackageDetailDTO;
import com.eflix.main.mapper.SubscriptionMapper;

import lombok.extern.log4j.Log4j2;

/**
 * 결제 처리를 담당하는 서비스
 * 
 * <p>
 * </p>
 * 
 * <h3>주요 기능</h3>
 * <ul>
 * <li>상품 조회</li>
 * </ul>
 * 
 * @author 복성민 (bokseongmin@gmail.com)
 * @version 1.0
 * @since 2025-06-19
 * 
 * @see
 * 
 * @changelog
 *            <ul>
 *            <li>2025-06-19: 최초 생성 (복성민)</li>
 *            <li>2025-06-20: 결제 코드 추가 (복성민)</li>
 *            <li>2025-07-04: 정기 결제 로직 추가 (복성민)</li>
 *            </ul>
 */

@Log4j2
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private SubscriptionMapper subscriptionMapper;

    @Override
    public PaymentEntity getPaymentById(String paymentIdx) {
        return paymentMapper.findPaymentById(paymentIdx);
    }

    @Override
    public int insertPayment(PaymentEntity paymentEntity) {
        return paymentMapper.insertPayment(paymentEntity);
    }

    @Override
    public int updatePayment(PaymentEntity paymentEntity) {
        return paymentMapper.updatePayment(paymentEntity);
    }

    @Override
    @Transactional
    public int insertSubscriptionInfo(SubscriptionDTO subscriptionDTO) {
        int affectedRows = subscriptionMapper.insertSubscription(subscriptionDTO);

        String spi_idx = "";
        if (affectedRows > 0) {
            spi_idx = subscriptionDTO.getSpiIdx();
        }

        affectedRows = subscriptionMapper.insertSubscriptionPackageDetail(null);

        return 0;
    }

    // 정기 구독
    @Override
    public void processSubscription() {
        log.info("정기 결제 요청 시작");

        LocalDate today = LocalDate.now();

        List<SubscriptionPaymentDTO> list = subscriptionMapper.findAllByStatus("SS01");

        List<SubscriptionPaymentDTO> _list = list.stream()
                .filter(dto -> dto.getSpiNext().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isEqual(today))
                .collect(Collectors.toList());
    }
}
