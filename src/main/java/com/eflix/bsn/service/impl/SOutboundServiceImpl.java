package com.eflix.bsn.service.impl;

import com.eflix.bsn.dto.OrdersDTO;
import com.eflix.bsn.dto.SalesOutboundDTO;
import com.eflix.bsn.dto.SoutboundDetailDTO;
import com.eflix.bsn.mapper.OrdersMapper;
import com.eflix.bsn.mapper.SOutboundMapper;
import com.eflix.bsn.service.SOutboundService;
import com.eflix.common.security.auth.AuthUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SOutboundServiceImpl implements SOutboundService {

    private final SOutboundMapper outboundMapper;
    private final OrdersMapper ordersMapper; // 주문서 상태 업데이트를 위해 추가

    @Override
    @Transactional(readOnly = true)
    public List<SalesOutboundDTO> getOutboundList() {
        String coIdx = AuthUtil.getCoIdx();
        return outboundMapper.selectAllOutbound(coIdx);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SoutboundDetailDTO> getOutboundDetails(String outboundNo) {
        return outboundMapper.selectOutboundDetails(outboundNo);
    }

    @Override
    public String createOutbound(SalesOutboundDTO dto) {
        String coIdx = AuthUtil.getCoIdx();
        dto.setCoIdx(coIdx);

        // 1. 출하번호 생성
        if (!StringUtils.hasText(dto.getOutboundNo())) {
            dto.setOutboundNo(generateNextOutboundNo());
        }
        
        // 2. 기본값 설정
        if (!StringUtils.hasText(dto.getWriteDt())) {
            dto.setWriteDt(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        if (dto.getOutboundDt() == null) {
            dto.setOutboundDt(java.sql.Date.valueOf(LocalDate.now()));
        }

        // 3. 헤더 저장
        outboundMapper.insertOutbound(dto);

        // 4. 상세 저장
        if (dto.getDetails() != null && !dto.getDetails().isEmpty()) {
            prepareDetailData(dto.getDetails(), dto.getOutboundNo(), coIdx);
            outboundMapper.insertOutboundDetailBatch(dto.getDetails());
        }

        log.info("출하 의뢰서 생성 완료: {}", dto.getOutboundNo());
        return dto.getOutboundNo();
    }
    
    @Override
    public String createOutboundFromOrder(String orderNo) {
        String coIdx = AuthUtil.getCoIdx();
        log.info("주문서 기반 출하 의뢰서 생성 시작 - 주문번호: {}", orderNo);

        // 1. 주문서 정보 조회
        SalesOutboundDTO outboundHeader = outboundMapper.selectOrderForOutbound(orderNo, coIdx);
        if (outboundHeader == null) {
            throw new IllegalArgumentException("주문서를 찾을 수 없습니다: " + orderNo);
        }

        List<SoutboundDetailDTO> outboundDetails = outboundMapper.selectOrderDetailsForOutbound(orderNo, coIdx);
        if (outboundDetails == null || outboundDetails.isEmpty()) {
            throw new IllegalArgumentException("출고할 품목이 없는 주문서입니다: " + orderNo);
        }
        outboundHeader.setDetails(outboundDetails);

        // 2. 출하 의뢰서 생성 (기존 로직 재사용)
        String outboundNo = createOutbound(outboundHeader);

        // 3. 원본 주문서의 출고 상태 업데이트
        try {
            OrdersDTO orderToUpdate = new OrdersDTO();
            orderToUpdate.setOrderNo(orderNo);
            orderToUpdate.setCoIdx(coIdx);
            // 예시: 주문서의 비고란에 출고번호 기록
            OrdersDTO existingOrder = ordersMapper.selectOrderHeader(orderToUpdate);
            if(existingOrder != null) {
                existingOrder.setRemarks((existingOrder.getRemarks() == null ? "" : existingOrder.getRemarks()) + " [출고완료: " + outboundNo + "]");
                ordersMapper.updateOrder(existingOrder);
            }
        } catch(Exception e) {
            log.error("주문서 상태 업데이트 실패 - 주문번호: {}", orderNo, e);
            // 출고는 생성되었으므로 이 오류가 전체 트랜잭션을 롤백시키지 않도록 처리
        }

        log.info("주문서 기반 출하 의뢰서 생성 완료: {} -> {}", orderNo, outboundNo);
        return outboundNo;
    }

    @Override
    public void updateOutbound(SalesOutboundDTO dto) {
        String coIdx = AuthUtil.getCoIdx();
        dto.setCoIdx(coIdx);
        
        // 1. 헤더 업데이트
        outboundMapper.updateOutbound(dto);

        // 2. 상세 업데이트 (기존 상세 모두 삭제 후 재등록)
        outboundMapper.deleteOutbound(dto.getOutboundNo(), coIdx); // Cascade로 상세 삭제
        outboundMapper.insertOutbound(dto); // 헤더 다시 삽입
        if (dto.getDetails() != null && !dto.getDetails().isEmpty()) {
            prepareDetailData(dto.getDetails(), dto.getOutboundNo(), coIdx);
            outboundMapper.insertOutboundDetailBatch(dto.getDetails());
        }
        log.info("출하 의뢰서 수정 완료: {}", dto.getOutboundNo());
    }

    @Override
    public void deleteOutbound(String outboundNo) {
        String coIdx = AuthUtil.getCoIdx();
        int result = outboundMapper.deleteOutbound(outboundNo, coIdx);
        if (result == 0) {
            throw new IllegalArgumentException("삭제할 출하 의뢰서가 없거나 권한이 없습니다.");
        }
        log.info("출하 의뢰서 삭제 완료: {}", outboundNo);
    }
    
    @Override
    @Transactional(readOnly = true)
    public String generateNextOutboundNo() {
        String coIdx = AuthUtil.getCoIdx();
        return outboundMapper.selectNextOutboundNo(coIdx);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getOutboundStatusStats() {
        String coIdx = AuthUtil.getCoIdx();
        return outboundMapper.selectOutboundStatusStats(coIdx);
    }

    private void prepareDetailData(List<SoutboundDetailDTO> details, String outboundNo, String coIdx) {
        AtomicInteger lineNo = new AtomicInteger(1);
        details.forEach(detail -> {
            detail.setOutboundNo(outboundNo);
            detail.setCoIdx(coIdx);
            detail.setLineNo(lineNo.getAndIncrement());
            if (!StringUtils.hasText(detail.getOutboundStatus())) {
                detail.setOutboundStatus("대기");
            }
            detail.calculateAmounts();
        });
    }
}
