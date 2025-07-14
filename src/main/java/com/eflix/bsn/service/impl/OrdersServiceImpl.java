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
        String coIdx = AuthUtil.getCoIdx();
        return ordersMapper.selectAllOrders(coIdx);
    }

    /** 단건 헤더 + 디테일 */
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

    /** 디테일만 조회 (그리드 AJAX) */
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

    /** 신규·수정 저장 (수정된 버전) */
    @Override
    public String saveOrder(OrdersDTO dto) {
        String coIdx = AuthUtil.getCoIdx();
        String empIdx = AuthUtil.getEmpIdx();
        dto.setCoIdx(coIdx);
        
        log.info("=== 주문서 저장 시작 ===");
        log.info("회사: {}, 사원: {}", coIdx, empIdx);
        log.info("요청 DTO: orderNo={}, customerCd={}, orderWriter={}, orderDt={}", 
                dto.getOrderNo(), dto.getCustomerCd(), dto.getOrderWriter(), dto.getOrderDt());
        log.info("디테일 건수: {}", dto.getDetails() != null ? dto.getDetails().size() : 0);
        
        // 필수 필드 검증
        try {
            validateOrderData(dto);
            log.info("주문 데이터 검증 완료");
        } catch (Exception e) {
            log.error("주문 데이터 검증 실패: {}", e.getMessage());
            throw e;
        }

        // ★★★ 수정된 신규/수정 구분 로직 ★★★
        // 데이터베이스에 실제로 존재하는지 확인하여 신규/수정 여부 판단
        OrdersDTO queryDto = new OrdersDTO();
        queryDto.setOrderNo(dto.getOrderNo());
        queryDto.setCoIdx(coIdx);
        OrdersDTO existingOrder = ordersMapper.selectOrderHeader(queryDto);
        boolean isNew = (existingOrder == null);

        log.info("저장 모드: {} (주문번호: {})", isNew ? "신규" : "수정", dto.getOrderNo());
        
        try {
            if (isNew) {
                // ★★★ 신규 등록 로직 ★★★
                log.info("신규 주문 등록 시작 - 주문번호: {}", dto.getOrderNo());
                
                // 필수 필드 기본값 설정
                setDefaultValues(dto, empIdx);
                
                // 헤더 INSERT
                int headerResult = ordersMapper.insertOrder(dto);
                if (headerResult <= 0) {
                    throw new RuntimeException("주문 헤더 저장 실패");
                }
                log.info("헤더 INSERT 성공");
                
                // 디테일 INSERT
                if (dto.getDetails() != null && !dto.getDetails().isEmpty()) {
                    prepareDetailData(dto, coIdx);
                    int detailResult = ordersMapper.insertOrderDetailBatch(dto.getDetails());
                    if (detailResult <= 0) {
                        throw new RuntimeException("주문 디테일 저장 실패");
                    }
                    log.info("디테일 INSERT 성공 - {} 건", dto.getDetails().size());
                }
                
            } else {
                // ★★★ 수정 로직 ★★★
                log.info("기존 주문 수정 시작 - 주문번호: {}", dto.getOrderNo());
                
                // 헤더 UPDATE
                int headerResult = ordersMapper.updateOrder(dto);
                if (headerResult <= 0) {
                    throw new RuntimeException("주문 헤더 수정 실패");
                }
                log.info("헤더 UPDATE 성공");
                
                // 디테일 수정: 기존 데이터 삭제 후 재삽입
                if (dto.getDetails() != null && !dto.getDetails().isEmpty()) {
                    // 기존 디테일 삭제
                    ordersMapper.deleteOrderDetailAll(dto.getOrderNo(), coIdx);
                    log.info("기존 디테일 삭제 완료");
                    
                    // 새 디테일 삽입
                    prepareDetailData(dto, coIdx);
                    int detailResult = ordersMapper.insertOrderDetailBatch(dto.getDetails());
                    if (detailResult <= 0) {
                        throw new RuntimeException("주문 디테일 수정 실패");
                    }
                    log.info("새 디테일 INSERT 성공 - {} 건", dto.getDetails().size());
                }
            }

            log.info("=== 주문서 저장 완료 ===");
            return dto.getOrderNo();
            
        } catch (Exception e) {
            log.error("=== 주문서 저장 실패 ===");
            log.error("회사: {}, 주문번호: {}, 오류: {}", coIdx, dto.getOrderNo(), e.getMessage());
            
            String userMessage = "주문서 저장 중 오류가 발생했습니다.";
            if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
                userMessage = "데이터베이스 제약 조건 위반 오류가 발생했습니다.";
            } else if (e instanceof IllegalArgumentException) {
                userMessage = e.getMessage();
            }
            
            throw new RuntimeException(userMessage, e);
        }
    }

    /**
     * 주문 데이터 검증
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
        
        if (dto.getDetails() == null || dto.getDetails().isEmpty()) {
            throw new IllegalArgumentException("주문 상세 항목이 없습니다.");
        }
        
        for (OrdersDetailDTO detail : dto.getDetails()) {
            if (!StringUtils.hasText(detail.getItemCode())) {
                throw new IllegalArgumentException("품목 코드는 필수입니다.");
            }
        }
    }

    /**
     * 기본값 설정
     */
    private void setDefaultValues(OrdersDTO dto, String empIdx) {
        if (!StringUtils.hasText(dto.getPaymentTerms())) {
            dto.setPaymentTerms("Net 30");
        }
        
        if (!StringUtils.hasText(dto.getOrderWriter())) {
            dto.setOrderWriter(empIdx);
        }
    }

    /**
     * 디테일 데이터 준비
     */
    private void prepareDetailData(OrdersDTO dto, String coIdx) {
        AtomicInteger seq = new AtomicInteger(1);
        
        dto.getDetails().forEach(detail -> {
            detail.setOrderNo(dto.getOrderNo());
            detail.setLineNo(seq.getAndIncrement());
            detail.setCoIdx(coIdx);
            
            // 기본값 설정
            if (detail.getQty() == null || detail.getQty().doubleValue() <= 0) {
                detail.setQty(java.math.BigDecimal.ONE);
            }
            
            if (!StringUtils.hasText(detail.getOutState())) {
                detail.setOutState("대기");
            }
        });
    }

    /** 헤더만 업데이트 */
    @Transactional
    @Override
    public void updateOrderHeader(OrdersDTO dto) {
        ordersMapper.updateOrder(dto);
    }

    /** 디테일 업데이트 */
    @Transactional
    @Override
    public void updateOrderDetails(String orderNo, List<OrdersDetailDTO> details) {
        for (OrdersDetailDTO d : details) {
            d.setOrderNo(orderNo);
            ordersMapper.updateOrderDetail(d);
        }
    }

    /*────────────────────── 삭제 영역 ──────────────────────*/

    @Override
    @Transactional
    public void deleteOrder(String orderNo) {
        String coIdx = AuthUtil.getCoIdx();
        // 디테일 먼저 삭제
        ordersMapper.deleteOrderDetailAll(orderNo, coIdx);
        // 헤더 삭제
        ordersMapper.deleteOrder(orderNo, coIdx);
    }

    /*────────────────── 주문번호 채번 ───────────────────*/

    @Override
    @Transactional(readOnly = true)
    public String generateNextOrderNo() {
        String coIdx = AuthUtil.getCoIdx();
        return ordersMapper.selectNextOrderNo(coIdx);
    }
}