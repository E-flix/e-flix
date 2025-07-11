package com.eflix.bsn.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.eflix.bsn.dto.SalesOutboundDTO;
import com.eflix.bsn.dto.SoutboundDetailDTO;
import com.eflix.bsn.mapper.SOutboundMapper;
import com.eflix.bsn.service.SOutboundService;
import com.eflix.common.security.auth.AuthUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SOutboundServiceImpl implements SOutboundService {

    private final SOutboundMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<SalesOutboundDTO> getOutboundList() {
        String coIdx = AuthUtil.getCoIdx();
        return mapper.selectAllOutbound(coIdx);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SoutboundDetailDTO> getOutboundDetails(String outboundNo) {
        return mapper.selectOutboundDetails(outboundNo);
    }

    @Override
    @Transactional
    public String createOutbound(SalesOutboundDTO dto) {
        String coIdx = AuthUtil.getCoIdx();
        String empIdx = AuthUtil.getEmpIdx();
        
        log.info("=== 출하 의뢰서 등록 시작 ===");
        log.info("회사: {}, 사원: {}", coIdx, empIdx);
        
        try {
            // ★ 1. 출하번호 생성 (자동 채번)
            if (!StringUtils.hasText(dto.getOutboundNo())) {
                String outboundNo = generateNextOutboundNo();
                dto.setOutboundNo(outboundNo);
                log.info("출하번호 생성: {}", outboundNo);
            }
            
            // ★ 2. 회사 정보 설정
            dto.setCoIdx(coIdx);
            
            // ★ 3. 기본값 설정
            if (!StringUtils.hasText(dto.getWriteDt())) {
                dto.setWriteDt(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
            if (!StringUtils.hasText(dto.getMoney())) {
                dto.setMoney("KRW");
            }
            
            // ★ 4. 필수 필드 검증
            validateOutboundData(dto);
            
            // ★ 5. 헤더 저장
            int headerResult = mapper.insertOutbound(dto);
            if (headerResult <= 0) {
                throw new RuntimeException("출하 의뢰서 헤더 저장에 실패했습니다.");
            }
            log.info("출하 의뢰서 헤더 저장 완료");
            
            // ★ 6. 상세 저장 (배치 처리) - OUTBOUND_STATUS 기본값 설정
            if (dto.getDetails() != null && !dto.getDetails().isEmpty()) {
                prepareDetailData(dto.getDetails(), dto.getOutboundNo(), coIdx);
                
                int detailResult = mapper.insertOutboundDetailBatch(dto.getDetails());
                if (detailResult <= 0) {
                    throw new RuntimeException("출하 의뢰서 상세 저장에 실패했습니다.");
                }
                log.info("출하 의뢰서 상세 저장 완료 - {} 건", dto.getDetails().size());
            }
            
            log.info("=== 출하 의뢰서 등록 완료 ===");
            return dto.getOutboundNo();
            
        } catch (Exception e) {
            log.error("출하 의뢰서 등록 실패 - 회사: {}, 오류: {}", coIdx, e.getMessage(), e);
            throw new RuntimeException("출하 의뢰서 등록 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String generateNextOutboundNo() {
        String coIdx = AuthUtil.getCoIdx();
        String nextNo = mapper.selectNextOutboundNo(coIdx);
        
        // ★ DB에서 생성 실패 시 대체 로직
        if (!StringUtils.hasText(nextNo)) {
            String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            nextNo = "OUT-" + today + "-0001";
        }
        
        log.info("출하번호 생성: {}", nextNo);
        return nextNo;
    }

    @Override
    @Transactional
    public void updateOutbound(SalesOutboundDTO dto) {
        String coIdx = AuthUtil.getCoIdx();
        dto.setCoIdx(coIdx);
        
        validateOutboundData(dto);
        
        int result = mapper.updateOutbound(dto);
        if (result <= 0) {
            throw new RuntimeException("출하 의뢰서 정보 수정에 실패했습니다.");
        }
        
        log.info("출하 의뢰서 정보 수정 완료 - 출하번호: {}", dto.getOutboundNo());
    }

    @Override
    @Transactional
    public void deleteOutbound(String outboundNo) {
        String coIdx = AuthUtil.getCoIdx();
        
        int result = mapper.deleteOutbound(outboundNo, coIdx);
        if (result <= 0) {
            throw new RuntimeException("출하 의뢰서 정보 삭제에 실패했습니다.");
        }
        
        log.info("출하 의뢰서 정보 삭제 완료 - 출하번호: {}", outboundNo);
    }

    @Override
    @Transactional
    public String createOutboundFromOrder(String orderNo) {
        String coIdx = AuthUtil.getCoIdx();
        
        log.info("=== 주문서 기반 출하 의뢰서 생성 시작 ===");
        log.info("주문번호: {}, 회사: {}", orderNo, coIdx);
        
        try {
            // ★ 1. 주문서 헤더 정보 조회
            SalesOutboundDTO outboundHeader = mapper.selectOrderForOutbound(orderNo, coIdx);
            if (outboundHeader == null) {
                throw new RuntimeException("주문서를 찾을 수 없습니다: " + orderNo);
            }
            
            // ★ 2. 주문서 상세 정보 조회
            List<SoutboundDetailDTO> outboundDetails = mapper.selectOrderDetailsForOutbound(orderNo, coIdx);
            if (outboundDetails == null || outboundDetails.isEmpty()) {
                throw new RuntimeException("주문서 상세 정보가 없습니다: " + orderNo);
            }
            
            // ★ 3. 출하 정보 설정
            outboundHeader.setDetails(outboundDetails);
            outboundHeader.setOutboundDt(java.sql.Date.valueOf(LocalDate.now())); // 출하일을 오늘로 설정
            
            // ★ 4. 출하 의뢰서 등록
            String outboundNo = createOutbound(outboundHeader);
            
            log.info("=== 주문서 기반 출하 의뢰서 생성 완료 ===");
            log.info("주문번호: {} → 출하번호: {}", orderNo, outboundNo);
            
            return outboundNo;
            
        } catch (Exception e) {
            log.error("주문서 기반 출하 의뢰서 생성 실패 - 주문번호: {}, 오류: {}", orderNo, e.getMessage(), e);
            throw new RuntimeException("주문서 기반 출하 의뢰서 생성 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    /**
     * ★ 출하 상세 항목의 상태 개별 업데이트
     */
    @Override
    @Transactional
    public void updateOutboundDetailStatus(String outboundNo, Integer lineNo, String outboundStatus) {
        String coIdx = AuthUtil.getCoIdx();
        
        log.info("출하 상태 업데이트 시작 - 출하번호: {}, 라인: {}, 상태: {}", outboundNo, lineNo, outboundStatus);
        
        // ★ 상태 유효성 검증
        if (!isValidOutboundStatus(outboundStatus)) {
            throw new IllegalArgumentException("유효하지 않은 출고 상태입니다: " + outboundStatus);
        }
        
        try {
            // ★ 현재 상세 정보 조회
            List<SoutboundDetailDTO> details = mapper.selectOutboundDetails(outboundNo);
            SoutboundDetailDTO targetDetail = details.stream()
                .filter(detail -> detail.getLineNo().equals(lineNo))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("해당 라인을 찾을 수 없습니다: " + lineNo));
            
            // ★ 상태 업데이트
            targetDetail.setOutboundStatus(outboundStatus);
            targetDetail.setCoIdx(coIdx);
            
            // ★ DB 업데이트
            int result = mapper.updateOutboundDetail(targetDetail);
            if (result <= 0) {
                throw new RuntimeException("출하 상태 업데이트에 실패했습니다.");
            }
            
            log.info("출하 상태 업데이트 완료 - 출하번호: {}, 라인: {}, 상태: {}", outboundNo, lineNo, outboundStatus);
            
        } catch (Exception e) {
            log.error("출하 상태 업데이트 실패 - 출하번호: {}, 라인: {}, 상태: {}, 오류: {}", 
                    outboundNo, lineNo, outboundStatus, e.getMessage(), e);
            throw new RuntimeException("출하 상태 업데이트 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    /**
     * ★ 출하 상태 일괄 업데이트
     */
    @Override
    @Transactional
    public void bulkUpdateOutboundStatus(String outboundNo, List<Integer> lineNos, String outboundStatus) {
        String coIdx = AuthUtil.getCoIdx();
        
        log.info("출하 상태 일괄 업데이트 시작 - 출하번호: {}, 라인수: {}, 상태: {}", 
                outboundNo, lineNos.size(), outboundStatus);
        
        // ★ 상태 유효성 검증
        if (!isValidOutboundStatus(outboundStatus)) {
            throw new IllegalArgumentException("유효하지 않은 출고 상태입니다: " + outboundStatus);
        }
        
        try {
            for (Integer lineNo : lineNos) {
                updateOutboundDetailStatus(outboundNo, lineNo, outboundStatus);
            }
            
            log.info("출하 상태 일괄 업데이트 완료 - 출하번호: {}, 처리된 라인수: {}", outboundNo, lineNos.size());
            
        } catch (Exception e) {
            log.error("출하 상태 일괄 업데이트 실패 - 출하번호: {}, 오류: {}", outboundNo, e.getMessage(), e);
            throw new RuntimeException("출하 상태 일괄 업데이트 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    /**
     * ★ 출하 데이터 검증
     */
    private void validateOutboundData(SalesOutboundDTO dto) {
        if (!StringUtils.hasText(dto.getOutboundNo())) {
            throw new IllegalArgumentException("출하번호는 필수입니다.");
        }
        if (!StringUtils.hasText(dto.getCustomerCd())) {
            throw new IllegalArgumentException("거래처 코드는 필수입니다.");
        }
        if (dto.getOutboundDt() == null) {
            throw new IllegalArgumentException("출하일자는 필수입니다.");
        }
    }

    /**
     * ★ 상세 데이터 준비 (라인번호 설정, 금액 계산, OUTBOUND_STATUS 기본값 설정)
     */
    private void prepareDetailData(List<SoutboundDetailDTO> details, String outboundNo, String coIdx) {
        AtomicInteger lineNo = new AtomicInteger(1);
        
        details.forEach(detail -> {
            detail.setOutboundNo(outboundNo);
            detail.setLineNo(lineNo.getAndIncrement());
            detail.setCoIdx(coIdx);
            
            // ★ OUTBOUND_STATUS 기본값 설정
            if (!StringUtils.hasText(detail.getOutboundStatus())) {
                detail.setOutboundStatus("대기");
            }
            
            // ★ 상태 유효성 검증
            if (!isValidOutboundStatus(detail.getOutboundStatus())) {
                log.warn("유효하지 않은 출고 상태가 감지되어 '대기'로 설정합니다: {}", detail.getOutboundStatus());
                detail.setOutboundStatus("대기");
            }
            
            // ★ 금액 자동 계산
            detail.calculateAmounts();
            
            log.debug("출하 상세 준비: 라인{} - {} (상태: {}, 금액: {})", 
                detail.getLineNo(), detail.getItemName(), detail.getOutboundStatus(), detail.getTotalAmount());
        });
    }

    /**
     * ★ 출고 상태 유효성 검증
     */
    private boolean isValidOutboundStatus(String status) {
        if (status == null) return false;
        return status.equals("대기") || 
               status.equals("준비중") || 
               status.equals("출고중") || 
               status.equals("출고완료") || 
               status.equals("취소");
    }

    /**
     * ★ 출하 상태별 통계 조회 (대시보드용)
     */
    @Override
    @Transactional(readOnly = true)
    public List<java.util.Map<String, Object>> getOutboundStatusStats() {
        String coIdx = AuthUtil.getCoIdx();
        
        try {
            // return mapper.selectOutboundStatusStats(coIdx);  // 실제 구현 시 주석 해제
            
            // ★ 임시 데이터 반환 (테스트용)
            return List.of(
                java.util.Map.of("status", "대기", "count", 15, "totalAmount", 5000000),
                java.util.Map.of("status", "준비중", "count", 8, "totalAmount", 3200000),
                java.util.Map.of("status", "출고중", "count", 5, "totalAmount", 2100000),
                java.util.Map.of("status", "출고완료", "count", 42, "totalAmount", 18500000),
                java.util.Map.of("status", "취소", "count", 2, "totalAmount", 800000)
            );
        } catch (Exception e) {
            log.error("출하 상태별 통계 조회 실패 - 회사: {}", coIdx, e);
            return List.of();
        }
    }

    /**
     * ★ 출하 지연 현황 조회 (3일 이상 대기 상태)
     */
    @Override
    @Transactional(readOnly = true)
    public List<java.util.Map<String, Object>> getDelayedOutbounds() {
        String coIdx = AuthUtil.getCoIdx();
        
        try {
            // return mapper.selectDelayedOutbounds(coIdx);  // 실제 구현 시 주석 해제
            
            // ★ 임시 데이터 반환 (테스트용)
            return List.of(
                java.util.Map.of("outboundNo", "OUT-20250108-001", "customerName", "ABC상사", 
                               "writeDt", "2025-01-05", "itemCount", 3, "totalAmount", 1500000, "delayDays", 3),
                java.util.Map.of("outboundNo", "OUT-20250107-002", "customerName", "XYZ코퍼레이션", 
                               "writeDt", "2025-01-04", "itemCount", 2, "totalAmount", 800000, "delayDays", 4)
            );
        } catch (Exception e) {
            log.error("출하 지연 현황 조회 실패 - 회사: {}", coIdx, e);
            return List.of();
        }
    }
}