package com.eflix.bsn.service.impl;

import com.eflix.bsn.dto.SalesOutboundDTO;
import com.eflix.bsn.dto.SoutboundDetailDTO;
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

        // 1. 출하번호가 없으면 새로 채번
        if (!StringUtils.hasText(dto.getOutboundNo())) {
            dto.setOutboundNo(generateNextOutboundNo());
        }
        
        // 2. 작성일, 출고일 등 기본값을 설정
        if (!StringUtils.hasText(dto.getWriteDt())) {
            dto.setWriteDt(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        if (dto.getOutboundDt() == null) {
            dto.setOutboundDt(java.sql.Date.valueOf(LocalDate.now()));
        }

        // 3. 헤더 정보를 데이터베이스에 저장
        outboundMapper.insertOutbound(dto);

        // 4. 상세 품목 정보가 있으면 함께 저장
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

        // 1. 주문서 정보를 조회하여 출하 의뢰서 데이터를 구성
        SalesOutboundDTO outboundHeader = outboundMapper.selectOrderForOutbound(orderNo, coIdx);
        if (outboundHeader == null) {
            throw new IllegalArgumentException("주문서를 찾을 수 없습니다: " + orderNo);
        }

        List<SoutboundDetailDTO> outboundDetails = outboundMapper.selectOrderDetailsForOutbound(orderNo, coIdx);
        if (outboundDetails == null || outboundDetails.isEmpty()) {
            throw new IllegalArgumentException("출고할 품목이 없는 주문서입니다: " + orderNo);
        }
        outboundHeader.setDetails(outboundDetails);

        // 2. 공통 생성 로직을 호출하여 출하 의뢰서를 최종 생성
        String outboundNo = createOutbound(outboundHeader);

        log.info("주문서 기반 출하 의뢰서 생성 완료: {} -> {}", orderNo, outboundNo);
        return outboundNo;
    }

    @Override
    public void updateOutbound(SalesOutboundDTO dto) {
        String coIdx = AuthUtil.getCoIdx();
        dto.setCoIdx(coIdx);
        
        // 헤더 정보만 수정합니다. (상세 정보 수정은 별도 로직 필요)
        int updatedRows = outboundMapper.updateOutbound(dto);
        if (updatedRows == 0) {
            throw new RuntimeException("수정할 출하 의뢰서가 없거나 권한이 없습니다: " + dto.getOutboundNo());
        }
        
        log.info("출하 의뢰서 헤더 수정 완료: {}", dto.getOutboundNo());
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
        String nextNo = outboundMapper.selectNextOutboundNo(coIdx);
        // DB에서 채번 실패 시 대체 번호 생성
        if (!StringUtils.hasText(nextNo)) {
            String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            nextNo = "OUT-" + today + "-0001";
        }
        return nextNo;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getOutboundStatusStats() {
        String coIdx = AuthUtil.getCoIdx();
        return outboundMapper.selectOutboundStatusStats(coIdx);
    }

    /**
     * 상세 품목 데이터를 일괄 저장하기 전에 필요한 값(출하번호, 순번 등)을 설정
     */
    private void prepareDetailData(List<SoutboundDetailDTO> details, String outboundNo, String coIdx) {
        AtomicInteger lineNo = new AtomicInteger(1);
        details.forEach(detail -> {
            detail.setOutboundNo(outboundNo);
            detail.setCoIdx(coIdx);
            detail.setLineNo(lineNo.getAndIncrement());
            if (!StringUtils.hasText(detail.getOutboundStatus())) {
                detail.setOutboundStatus("대기");
            }
        });
    }
}
