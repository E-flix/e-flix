package com.eflix.bsn.service.impl;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.eflix.bsn.dto.OrdersDTO;
import com.eflix.bsn.dto.OrdersDetailDTO;
import com.eflix.bsn.mapper.OrdersMapper;
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

    @Autowired
    private final OrdersMapper ordersMapper;

    /*────────────────────── 조회 영역 ──────────────────────*/

    /** 헤더 목록 (헤더 그리드) */
    @Override
    @Transactional(readOnly = true)
    public List<OrdersDTO> getOrdersList() { 
        String coIdx = AuthUtil.getCoIdx(); // ★ AuthUtil 사용
        return ordersMapper.selectAllOrders(coIdx);
    }

    /** 단건 헤더 + 디테일 */
    @Override
    @Transactional(readOnly = true)
    public OrdersDTO getOrder(String orderNo) {
        String coIdx = AuthUtil.getCoIdx(); // ★ AuthUtil 사용
        
        OrdersDTO dto = new OrdersDTO();
        dto.setCoIdx(coIdx);
        dto.setOrderNo(orderNo);

        OrdersDetailDTO detailDTO = new OrdersDetailDTO();
        detailDTO.setCoIdx(coIdx);
        detailDTO.setOrderNo(orderNo);
        
        OrdersDTO header = ordersMapper.selectOrderHeader(dto);        // 헤더
        if (header == null) return new OrdersDTO();                        // Not-found safety
        header.setDetails(ordersMapper.selectOrderDetails(detailDTO));       // 디테일
        return header;
    }

    /** ⭐ 디테일만 조회 (그리드 AJAX) */
    @Override
    @Transactional(readOnly = true)
    public List<OrdersDetailDTO> getOrderDetails(String orderNo) {
        String coIdx = AuthUtil.getCoIdx(); // ★ AuthUtil 사용
        
        OrdersDetailDTO detailDTO = new OrdersDetailDTO();
        detailDTO.setCoIdx(coIdx);
        detailDTO.setOrderNo(orderNo);
        return ordersMapper.selectOrderDetails(detailDTO);
    }

    /*────────────────────── 저장 영역 ──────────────────────*/

    /** 신규·수정 저장 (최종 강화 버전) */
    @Override
    public String saveOrder(OrdersDTO dto) {
        // ★ 1. 회사/사원 정보 설정
        String coIdx = AuthUtil.getCoIdx();
        String empIdx = AuthUtil.getEmpIdx();
        dto.setCoIdx(coIdx);
        
        log.info("=== 주문서 저장 시작 ===");
        log.info("회사: {}, 사원: {}", coIdx, empIdx);
        log.info("요청 DTO: orderNo={}, customerCd={}, orderWriter={}, orderDt={}", 
                dto.getOrderNo(), dto.getCustomerCd(), dto.getOrderWriter(), dto.getOrderDt());
        log.info("디테일 건수: {}", dto.getDetails() != null ? dto.getDetails().size() : 0);
        
        // ★ 2. 필수 필드 검증
        try {
            validateOrderData(dto);
            log.info("주문 데이터 검증 완료");
        } catch (Exception e) {
            log.error("주문 데이터 검증 실패: {}", e.getMessage());
            throw e;
        }
        
        boolean isNew = !StringUtils.hasText(dto.getOrderNo());
        log.info("저장 모드: {}", isNew ? "신규" : "수정");
        
        try {
            /* ① 신규라면 주문번호 채번 후 헤더 INSERT */
            if (isNew) {
                String newOrderNo = generateNextOrderNo();
                dto.setOrderNo(newOrderNo);
                log.info("새 주문번호 생성: {}", newOrderNo);
                
                // ★ 필수 필드 재검증 및 기본값 설정
                setDefaultValues(dto, empIdx);
                
                log.info("헤더 INSERT 시작 - 회사: {}, 주문번호: {}, 거래처: {}, 담당자: {}", 
                        coIdx, dto.getOrderNo(), dto.getCustomerCd(), dto.getOrderWriter());
                
                // ★ 헤더 INSERT 실행
                int headerResult = ordersMapper.insertOrder(dto);
                
                if (headerResult <= 0) {
                    log.error("헤더 INSERT 실패 - 반환값: {}", headerResult);
                    throw new RuntimeException("주문 헤더 저장에 실패했습니다. (반환값: " + headerResult + ")");
                }
                log.info("헤더 INSERT 성공 - 영향받은 행: {}", headerResult);
            }
            /* ② 수정이라면 헤더 UPDATE + 기존 디테일 삭제 */
            else {
                log.info("헤더 UPDATE 및 기존 디테일 삭제 시작");
                ordersMapper.updateOrder(dto);
                ordersMapper.deleteOrderDetailAll(dto.getOrderNo(), coIdx);
                log.info("헤더 UPDATE 및 기존 디테일 삭제 완료");
            }

            /* ③ 디테일 라인번호 부여 후 일괄 INSERT */
            if (dto.getDetails() != null && !dto.getDetails().isEmpty()) {
                // ★ 디테일 데이터 검증 및 설정 (회사 정보 포함)
                prepareDetailData(dto, coIdx);
                
                log.info("디테일 INSERT 시작 - 회사: {}, 건수: {}", coIdx, dto.getDetails().size());
                
                // ★ 각 디테일 로그
                for (int i = 0; i < dto.getDetails().size(); i++) {
                    OrdersDetailDTO detail = dto.getDetails().get(i);
                    log.info("디테일 {}: lineNo={}, itemCode={}, qty={}, unitPrice={}", 
                            i + 1, detail.getLineNo(), detail.getItemCode(), detail.getQty(), detail.getUnitPrice());
                }
                
                // ★ 디테일 INSERT 실행
                int detailResult = ordersMapper.insertOrderDetailBatch(dto.getDetails());
                
                if (detailResult <= 0) {
                    log.error("디테일 INSERT 실패 - 반환값: {}", detailResult);
                    throw new RuntimeException("주문 디테일 저장에 실패했습니다. (반환값: " + detailResult + ")");
                }
                log.info("디테일 INSERT 성공 - 영향받은 행: {}", detailResult);
            } else {
                log.warn("디테일 데이터가 없습니다.");
            }

            log.info("=== 주문서 저장 완료 ===");
            log.info("최종 주문번호: {}", dto.getOrderNo());
            return dto.getOrderNo();
            
        } catch (Exception e) {
            log.error("=== 주문서 저장 실패 ===");
            log.error("회사: {}, 주문번호: {}, 오류 클래스: {}", coIdx, dto.getOrderNo(), e.getClass().getSimpleName());
            log.error("오류 메시지: {}", e.getMessage());
            log.error("스택 트레이스:", e);
            
            // ★ 구체적인 오류 메시지 제공
            String userMessage = "주문서 저장 중 오류가 발생했습니다.";
            if (e instanceof java.sql.SQLException) {
                userMessage = "데이터베이스 오류가 발생했습니다. 관리자에게 문의하세요.";
            } else if (e instanceof IllegalArgumentException) {
                userMessage = e.getMessage(); // 검증 오류는 사용자에게 직접 표시
            }
            
            throw new RuntimeException(userMessage, e);
        }
    }

    /**
     * ★ 주문 데이터 검증 (멀티테넌트)
     */
    private void validateOrderData(OrdersDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("주문 데이터가 없습니다.");
        }
        
        if (!StringUtils.hasText(dto.getCoIdx())) {
            throw new IllegalArgumentException("회사 정보가 없습니다.");
        }
        
        if (!StringUtils.hasText(dto.getCustomerCd())) {
            throw new IllegalArgumentException("거래처 코드는 필수입니다.");
        }
        
        if (dto.getOrderDt() == null) {
            throw new IllegalArgumentException("주문일자는 필수입니다.");
        }
        
        // 디테일 데이터 검증
        if (dto.getDetails() == null || dto.getDetails().isEmpty()) {
            throw new IllegalArgumentException("주문 상세 항목이 없습니다.");
        }
        
        for (OrdersDetailDTO detail : dto.getDetails()) {
            if (!StringUtils.hasText(detail.getItemCode())) {
                throw new IllegalArgumentException("품목 코드는 필수입니다.");
            }
        }
        
        log.info("주문 데이터 검증 완료 - 회사: {}, 주문번호: {}", dto.getCoIdx(), dto.getOrderNo());
    }

    /**
     * ★ 기본값 설정 (사원 정보 활용)
     */
    private void setDefaultValues(OrdersDTO dto, String empIdx) {
        // 결제조건 기본값
        if (!StringUtils.hasText(dto.getPaymentTerms())) {
            dto.setPaymentTerms("Net 30");
        }
        
        // ★ 담당자 기본값 (사원 코드 사용)
        if (!StringUtils.hasText(dto.getOrderWriter())) {
            dto.setOrderWriter(empIdx); // emp-101 형태
        }
        
        log.info("기본값 설정 완료 - 회사: {}, 담당자: {}, 결제조건: {}", 
                dto.getCoIdx(), dto.getOrderWriter(), dto.getPaymentTerms());
    }

    /**
     * ★ 디테일 데이터 준비 (회사 정보 설정)
     */
    private void prepareDetailData(OrdersDTO dto, String coIdx) {
        AtomicInteger seq = new AtomicInteger(1);
        
        dto.getDetails().forEach(detail -> {
            detail.setOrderNo(dto.getOrderNo());
            detail.setLineNo(seq.getAndIncrement());
            detail.setCoIdx(coIdx); // ★ 회사 정보 설정
            
            // 필수 필드 기본값 설정
            if (detail.getQty() == null || detail.getQty().doubleValue() <= 0) {
                detail.setQty(java.math.BigDecimal.ONE);
            }
            
            if (!StringUtils.hasText(detail.getOutState())) {
                detail.setOutState("대기");
            }
        });
        
        log.info("디테일 데이터 준비 완료 - 회사: {}, 건수: {}", coIdx, dto.getDetails().size());
    }

    /** 헤더만 업데이트 */
    @Transactional
    @Override
    public void updateOrderHeader(OrdersDTO dto) {
        ordersMapper.updateOrder(dto);
    }

     /** 단일 라인 아이템 업데이트 */
    @Transactional
    @Override
    public void updateOrderDetails(String orderNo, List<OrdersDetailDTO> details) {
         // 먼저 기존 라인 전체 삭제 후 재삽입하거나,
         // 또는 수정과 삽입을 구분하여 처리
        for (OrdersDetailDTO d : details) {
            d.setOrderNo(orderNo);
            ordersMapper.updateOrderDetail(d);
        }
    }

    /*────────────────────── 삭제 영역 ──────────────────────*/

    @Override
    @Transactional
    public void deleteOrder(String orderNo) {
        String coIdx = AuthUtil.getCoIdx(); // ★ AuthUtil 사용
        // ① 디테일 먼저 삭제
        ordersMapper.deleteOrderDetailAll(orderNo, coIdx);
        // ② 헤더 삭제
        ordersMapper.deleteOrder(orderNo, coIdx);
    }

    /*────────────────── 주문번호 채번 ───────────────────*/

    @Override
    @Transactional(readOnly = true)
    public String generateNextOrderNo() {
        String coIdx = AuthUtil.getCoIdx(); // ★ AuthUtil 사용
        return ordersMapper.selectNextOrderNo(coIdx);
    }
}