package com.eflix.common.payment.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eflix.common.exception.SyncPaymentException;
import com.eflix.common.payment.Entity.PaymentEntity;
import com.eflix.common.payment.config.PortOneSecretProperties;
import com.eflix.common.payment.dto.CompletePaymentRequest;
import com.eflix.common.payment.dto.CustomData;
import com.eflix.common.payment.dto.ItemDTO;
import com.eflix.common.payment.service.PaymentService;
import com.eflix.main.dto.SubscriptionPackageDTO;
import com.eflix.main.mapper.ModuleMapper;
import com.eflix.main.mapper.SubscriptionMapper;
import com.eflix.main.service.CompanyService;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.portone.sdk.server.common.Currency;
import io.portone.sdk.server.common.Currency.Krw;
import io.portone.sdk.server.payment.PaidPayment;
import io.portone.sdk.server.payment.PaymentClient;
import io.portone.sdk.server.webhook.WebhookTransaction;
import io.portone.sdk.server.webhook.WebhookVerifier;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * PortOne 결제 처리 컨트롤러
 * <p>
 * 이 클래스는 PortOne SDK를 사용하여 결제 처리, 검증, 웹훅 처리 등의
 * 결제 관련 API 엔드포인트를 제공합니다.
 * <br><br>
 * 상품 조회, 결제 완료 처리, 결제 동기화, 웹훅 이벤트 처리 등의
 * 핵심 결제 기능을 담당하며, 결제 데이터의 무결성 검증을 수행합니다.
 * </p>
 *
 * <h3>주요 기능</h3>
 * <ul>
 *   <li><b>상품 조회</b> : 등록된 상품 정보를 반환</li>
 *   <li><b>결제 완료 처리</b> : 클라이언트 결제 완료 후 서버 측 검증 및 동기화</li>
 *   <li><b>결제 동기화</b> : PortOne API를 통한 실제 결제 상태 확인 및 로컬 저장소 업데이트</li>
 *   <li><b>웹훅 처리</b> : PortOne에서 발송하는 결제 상태 변경 이벤트 처리</li>
 *   <li><b>결제 검증</b> : 결제 금액, 상품명, 통화 등의 데이터 무결성 검증</li>
 * </ul>
 *
 * @author 복성민 (bokseongmin@gmail.com)
 * @version 1.0
 * @since 2025-06-19
 *
 * @see PaymentClient
 * @see WebhookVerifier
 * @see PortOneSecretProperties
 *
 * @changelog
 * <ul>
 *   <li>2025-06-19: Kotlin에서 Java로 변환 및 최초 생성 (복성민)</li>
 * </ul>
 */

@Slf4j
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private static final Map<String, ItemDTO> items = new HashMap<>();

    // 샘플 데이터
    static {
        Currency krw = Krw.INSTANCE; // Kotlin의 object는 Java에서 INSTANCE로 접근
        items.put("shoes", new ItemDTO("shoes", "신발", 1000, krw.getValue()));
    }

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private SubscriptionMapper subscriptionMapper;

    @Autowired
    private ModuleMapper moduleMapper;

    @Autowired
    private PaymentClient paymentClient;
    
    @Autowired
    private WebhookVerifier webhookVerifier;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/item")
    public ItemDTO getItem(@RequestParam("spkIdx") String spkIdx) {
        SubscriptionPackageDTO subscriptionPackageDTO = subscriptionMapper.findById(spkIdx);

        Currency krw = Krw.INSTANCE; // Kotlin의 object는 Java에서 INSTANCE로 접근

        items.put(spkIdx, new ItemDTO(subscriptionPackageDTO.getSpkIdx(), subscriptionPackageDTO.getSpkName(), subscriptionPackageDTO.getSpkPrice().intValue(), krw.getValue()));
        return items.get(spkIdx);
    }

    @PostMapping("/complete")
    public CompletableFuture<PaymentEntity> completePayment(@RequestBody CompletePaymentRequest request ) {
        return syncPayment(request.getPaymentId());
    }

    public CompletableFuture<PaymentEntity> syncPayment(String paymentId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // 기존 결제 정보 조회 또는 생성
                PaymentEntity payment = paymentService.getPaymentById(paymentId);
                if (payment == null) {
                    payment = new PaymentEntity();
                    payment.setPaymentId(paymentId);
                    payment.setStatus("PENDING");
                    paymentService.insertPayment(payment);
                }

                // PortOne API에서 실제 결제 정보 조회
                var actualPaymentFuture = paymentClient.getPayment(paymentId);
                var actualPayment = actualPaymentFuture.join(); // CompletableFuture 처리

                // Java 8+ 호환 방식
                if (actualPayment instanceof PaidPayment) {
                    PaidPayment paidPayment = (PaidPayment) actualPayment;
                    
                    if (!verifyPayment(paidPayment)) {
                        log.error("결제 검증 실패 - Payment ID: {}, getStatusChangedAt: {}, Amount: {}",
                                paidPayment.getId(),
                                paidPayment.getStatusChangedAt(),
                                paidPayment.getAmount());
                        throw new SyncPaymentException("결제 검증에 실패했습니다.");
                    }
                    
                    log.info("결제 성공 {}", actualPayment);
                    
                    if (!"PAID".equals(payment.getStatus())) {
                        payment.setStatus("PAID");
                        paymentService.updatePayment(payment);
                    }
                    
                    return payment;
                } else {
                    throw new SyncPaymentException();
                }
            } catch (Exception e) {
                log.error("Payment sync failed for paymentId: {}", paymentId, e);
                throw new SyncPaymentException();
            }
        });
    }

    private boolean verifyPayment(PaidPayment payment) {
        String channelTypeValue = payment.getChannel().getType().getValue();
        log.info(channelTypeValue);
        // if (!"live".equalsIgnoreCase(channelTypeValue)) {
        //     return false;
        // }

        try {
            String customDataStr = payment.getCustomData();
            if (customDataStr == null) return false;
            
            CustomData customData = objectMapper.readValue(customDataStr, CustomData.class);
            ItemDTO item = items.get(customData.getItem());
            
            System.out.println("item" + item);
            if (item == null) return false;
            
            return Objects.equals(payment.getOrderName(), item.getName()) &&
                   payment.getAmount().getTotal() == item.getPrice() &&
                   Objects.equals(payment.getCurrency().getValue(), item.getCurrency());
        } catch (Exception e) {
            log.error("Payment verification failed", e);
            return false;
        }
    }

    @PostMapping("/webhook")
    public CompletableFuture<Void> handleWebhook(
            @RequestBody String body,
            @RequestHeader("webhook-id") String webhookId,
            @RequestHeader("webhook-timestamp") String webhookTimestamp,
            @RequestHeader("webhook-signature") String webhookSignature) {
        
        return CompletableFuture.runAsync(() -> {
            try {
                var webhook = webhookVerifier.verify(body, webhookId, webhookTimestamp, webhookSignature);
                
                if (webhook instanceof WebhookTransaction webhookTransaction) {
                    syncPayment(webhookTransaction.getData().getPaymentId()).join();
                }
            } catch (Exception e) {
                log.error("Webhook handling failed", e);
                throw new SyncPaymentException();
            }
        });
    }

    @PostMapping("/billings")
    public String billings(@RequestBody String body) {
        //TODO: process POST request
        System.out.println(body);
        return body;
    }
}