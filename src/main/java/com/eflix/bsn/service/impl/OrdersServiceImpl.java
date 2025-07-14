package com.eflix.bsn.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.eflix.bsn.dto.CreditInfoDTO;
import com.eflix.bsn.dto.OrdersDTO;
import com.eflix.bsn.dto.OrdersDetailDTO;
import com.eflix.bsn.mapper.OrdersMapper;
import com.eflix.bsn.service.CreditService;
import com.eflix.bsn.service.OrdersService;
import com.eflix.common.security.auth.AuthUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 주문(ORDERS) 도메인 서비스 구현 (멀티테넌트 지원)
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrdersServiceImpl implements OrdersService {

    private final OrdersMapper ordersMapper;
    private final CreditService creditService; // 여신 서비스 의존성 추가

    /*────────────────────── 조회 영역 ──────────────────────*/

    @Override
    @Transactional(readOnly = true)
    public List<OrdersDTO> getOrdersList() { 
        String coIdx = AuthUtil.getCoIdx();
        return ordersMapper.selectAllOrders(coIdx);
    }

    @Override
    @Transactional(readOnly = true)
    public OrdersDTO getOrder(String orderNo) {
        String coIdx = AuthUtil.getCoIdx();
        
        OrdersDTO dto = new OrdersDTO();
        dto.setCoIdx(coIdx);
        dto.setOrderNo(orderNo);

        OrdersDetailDTO detailDTO = new OrdersDetailDTO();
        detailDTO.setCoIdx(coIdx);
        detailDTO.setOrderNo(orderNo);
        
        OrdersDTO header = ordersMapper.selectOrderHeader(dto);
        if (header == null) return new OrdersDTO();
        header.setDetails(ordersMapper.selectOrderDetails(detailDTO));
        return header;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdersDetailDTO> getOrderDetails(String orderNo) {
        String coIdx = AuthUtil.getCoIdx();
        
        OrdersDetailDTO detailDTO = new OrdersDetailDTO();
        detailDTO.setCoIdx(coIdx);
        detailDTO.setOrderNo(orderNo);
        return ordersMapper.selectOrderDetails(detailDTO);
    }

    /*────────────────────── 저장 영역 ──────────────────────*/

    /**
     * [수정된 메서드]
     * 신규 주문서 저장 시, 여신 상태를 먼저 검증합니다.
     */
    @Override
    @Transactional
    public String saveOrder(OrdersDTO dto) {
        String coIdx = AuthUtil.getCoIdx();
        String empIdx = AuthUtil.getEmpIdx();
        dto.setCoIdx(coIdx);
        
        log.info("=== 주문서 저장 시작 ===");
        log.info("회사: {}, 사원: {}, 주문번호: {}", coIdx, empIdx, dto.getOrderNo());

        // 1. 데이터 유효성 검증
        validateOrderData(dto);
        log.info("주문 데이터 검증 완료");

        // 2. 여신 상태 검증 (Credit Check)
        validateCreditStatus(dto);
        log.info("여신 검증 통과 - 거래처 코드: {}", dto.getCustomerCd());
        
        // 3. 주문번호 중복 검증
        OrdersDTO queryDto = new OrdersDTO();
        queryDto.setOrderNo(dto.getOrderNo());
        queryDto.setCoIdx(coIdx);
        if (ordersMapper.selectOrderHeader(queryDto) != null) {
            log.error("주문서 저장 실패: 주문번호 중복. 주문번호: {}", dto.getOrderNo());
            throw new RuntimeException("이미 존재하는 주문번호 [" + dto.getOrderNo() + "] 입니다. 잠시 후 다시 시도해주세요.");
        }

        // 4. 모든 검증 통과 후 DB 저장
        try {
            log.info("신규 주문 등록 시작 - 주문번호: {}", dto.getOrderNo());
            
            setDefaultValues(dto, empIdx);
            
            int headerResult = ordersMapper.insertOrder(dto);
            if (headerResult <= 0) throw new RuntimeException("주문 헤더 저장 실패");
            
            if (dto.getDetails() != null && !dto.getDetails().isEmpty()) {
                prepareDetailData(dto, coIdx);
                int detailResult = ordersMapper.insertOrderDetailBatch(dto.getDetails());
                if (detailResult <= 0) throw new RuntimeException("주문 디테일 저장 실패");
            }

            log.info("=== 주문서 저장 완료 ===");
            return dto.getOrderNo();

        } catch (Exception e) {
            log.error("=== 주문서 저장 실패 ===");
            log.error("회사: {}, 주문번호: {}, 오류: {}", coIdx, dto.getOrderNo(), e.getMessage(), e);
            String userMessage = "주문서 저장 중 오류가 발생했습니다. 관리자에게 문의하세요.";
            if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
                userMessage = "데이터베이스 저장 오류가 발생했습니다. 입력값을 확인해주세요.";
            } else if (e instanceof RuntimeException) {
                userMessage = e.getMessage();
            }
            throw new RuntimeException(userMessage, e);
        }
    }

    /**
     * [신규 메서드]
     * 거래처의 여신 상태를 검증하는プライベート 메서드
     * @param orderDto 주문서 정보
     */
    private void validateCreditStatus(OrdersDTO orderDto) {
        String customerCd = orderDto.getCustomerCd();
        CreditInfoDTO creditInfo = creditService.getCreditInfo(customerCd);

        if (creditInfo == null) {
            log.warn("여신 정보를 찾을 수 없습니다. 거래처 코드: {}", customerCd);
            throw new RuntimeException("해당 거래처의 여신 정보가 등록되어 있지 않아 주문할 수 없습니다.");
        }

        // 거래 정지 또는 여신 보류 상태인지 확인
        if (!creditInfo.isTradeAvailable()) {
            String reason = creditInfo.getTradeUnavailableReason();
            log.warn("주문 불가 거래처의 주문 시도: {}, 사유: {}", customerCd, reason);
            throw new RuntimeException("주문이 불가능한 거래처입니다. (" + reason + ")");
        }

        // 여신 한도 초과 여부 확인
        BigDecimal remainingCredit = creditInfo.getRemainingCredit();
        if (remainingCredit == null) {
            log.error("여신 한도 계산 불가: 한도 또는 사용액이 null입니다. 거래처 코드: {}", customerCd);
            throw new RuntimeException("여신 한도를 계산할 수 없어 주문을 진행할 수 없습니다.");
        }
        
        // 이번 주문의 총 공급가액 계산 (부가세 제외)
        BigDecimal newOrderTotalAmount = orderDto.getDetails().stream()
            .map(detail -> detail.getSupplyAmount() != null ? detail.getSupplyAmount() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 남은 여신 < 이번 주문 금액
        if (remainingCredit.compareTo(newOrderTotalAmount) < 0) {
            log.warn("여신 한도 초과: 남은 한도={}, 주문 금액={}, 거래처={}", remainingCredit, newOrderTotalAmount, customerCd);
            throw new RuntimeException(
                String.format("여신 한도를 초과하여 주문할 수 없습니다. (잔여 한도: %,.0f / 주문 금액: %,.0f)", remainingCredit, newOrderTotalAmount)
            );
        }
    }

    private void validateOrderData(OrdersDTO dto) {
        if (dto == null) throw new IllegalArgumentException("주문 데이터가 없습니다.");
        if (!StringUtils.hasText(dto.getCustomerCd())) throw new IllegalArgumentException("거래처 코드는 필수입니다.");
        if (dto.getOrderDt() == null) throw new IllegalArgumentException("주문일자는 필수입니다.");
        if (dto.getDetails() == null || dto.getDetails().isEmpty()) throw new IllegalArgumentException("주문 상세 항목이 없습니다.");
    }

    private void setDefaultValues(OrdersDTO dto, String empIdx) {
        if (!StringUtils.hasText(dto.getPaymentTerms())) dto.setPaymentTerms("Net 30");
        if (!StringUtils.hasText(dto.getOrderWriter())) dto.setOrderWriter(empIdx);
    }

    private void prepareDetailData(OrdersDTO dto, String coIdx) {
        AtomicInteger seq = new AtomicInteger(1);
        dto.getDetails().forEach(detail -> {
            detail.setOrderNo(dto.getOrderNo());
            detail.setLineNo(seq.getAndIncrement());
            detail.setCoIdx(coIdx);
            if (detail.getQty() == null || detail.getQty().doubleValue() <= 0) detail.setQty(BigDecimal.ONE);
            if (!StringUtils.hasText(detail.getOutState())) detail.setOutState("대기");
        });
    }

    @Override
    public void updateOrderHeader(OrdersDTO dto) {
        ordersMapper.updateOrder(dto);
    }

    @Override
    public void updateOrderDetails(String orderNo, List<OrdersDetailDTO> details) {
        for (OrdersDetailDTO d : details) {
            d.setOrderNo(orderNo);
            ordersMapper.updateOrderDetail(d);
        }
    }

    @Override
    public void deleteOrder(String orderNo) {
        String coIdx = AuthUtil.getCoIdx();
        ordersMapper.deleteOrderDetailAll(orderNo, coIdx);
        ordersMapper.deleteOrder(orderNo, coIdx);
    }

    @Override
    @Transactional(readOnly = true)
    public synchronized String generateNextOrderNo() {
        String coIdx = AuthUtil.getCoIdx();
        LocalDate today = LocalDate.now();
        String datePart = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefix = "SO-" + datePart + "-";

        String lastOrderNo = ordersMapper.findLatestOrderNoForToday(prefix, coIdx);

        int nextSequence = 1;
        if (StringUtils.hasText(lastOrderNo)) {
            try {
                String sequencePart = lastOrderNo.substring(prefix.length());
                nextSequence = Integer.parseInt(sequencePart) + 1;
            } catch (Exception e) {
                log.error("주문번호 시퀀스 파싱 실패: {}", lastOrderNo, e);
            }
        }

        return prefix + String.format("%04d", nextSequence);
    }
}
