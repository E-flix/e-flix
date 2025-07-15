package com.eflix.common.payment.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.eflix.common.payment.Entity.PaymentEntity;
import com.eflix.common.payment.dto.SubscriptionPaymentDTO;
import com.eflix.common.payment.mapper.PaymentMapper;
import com.eflix.common.payment.service.BillingService;
import com.eflix.common.payment.service.PaymentService;
import com.eflix.main.mapper.SubscriptionMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
    private BillingService billingService;

    @Autowired
    private SubscriptionMapper subscriptionMapper;

    private final RestTemplate restTemplate = new RestTemplate();

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

    // 정기 구독
    @Override
    public void processSubscription() {
        log.info("정기 결제 요청 시작");

        LocalDate today = LocalDate.now();

        List<SubscriptionPaymentDTO> list = subscriptionMapper.findAllByStatus("SS01");

        List<SubscriptionPaymentDTO> targetList = list.stream()
                .filter(dto -> dto.getSpiNext().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isEqual(today))
                .collect(Collectors.toList());

        String accessToken = billingService.getAccessToken();

        for (SubscriptionPaymentDTO dto : targetList) {
            try {
                String result = requestPayment(dto, accessToken); // 결제 시도

                if (result.equals("sucess")) {
                    paymentMapper.callUpdateSubscription(dto.getSpiIdx());
                } else {
                    paymentMapper.insertPaymentLog(dto.getSpiIdx(), dto.getSpiStatus(), "ES04", "결제 실패 (PortOne 응답)");
                }

            } catch (Exception e) {
                paymentMapper.insertPaymentLog(dto.getSpiIdx(), dto.getSpiStatus(), "ES04", e.getMessage());
            }
        }
    }

    @Override
    public String requestPayment(SubscriptionPaymentDTO dto, String accessToken) {
        String url = "https://api.iamport.kr/subscribe/payments/again";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken); // Authorization: Bearer 토큰

        JsonObject json = new JsonObject();
        json.addProperty("customer_uid", dto.getSpiUid());    // 빌링키
        json.addProperty("merchant_uid", System.currentTimeMillis());    // 고유 주문 ID
        json.addProperty("amount", dto.getSpkPrice());        // 결제 금액
        json.addProperty("name", dto.getSpkName());           // 상품명
        json.addProperty("buyer_name", dto.getPschName());    // 구매자명
        json.addProperty("buyer_email", dto.getPschEmail());  // 이메일

        HttpEntity<String> requestEntity = new HttpEntity<>(json.toString(), headers);

        ResponseEntity<String> res = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class);

        String resBody = res.getBody();
        JsonObject root = JsonParser.parseString(resBody).getAsJsonObject();
        int code = root.get("code").getAsInt();

        if(code == 0) {
            System.out.println("결제 성공");
            return "success";
        } else {
            String message = root.get("message").getAsString();
            System.out.println("결제 실패");
            System.out.println(root.get("message").getAsString());
            return message;
        }
    }
}
