package com.eflix.bsn.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.eflix.bsn.dto.QuotationDTO;
import com.eflix.bsn.dto.QuotationDetailDTO;
import com.eflix.bsn.mapper.QuotationMapper;
import com.eflix.bsn.service.QuotationService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuotationServiceImpl implements QuotationService {

    private final QuotationMapper quotationMapper;
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Override
    public String generateNextQuotationNo() {
        String today = LocalDate.now().format(fmt);
        String lastNo = quotationMapper.findLastQuotationNo();  // ex) "QT-20250610-005"
        int nextSeq = 1;

        if (lastNo != null && lastNo.startsWith("QT-")) {
            try {
                String[] parts = lastNo.split("-");
                // 날짜가 오늘과 같을 때만 시퀀스 증가
                if (parts.length == 3 && parts[1].equals(today)) {
                    nextSeq = Integer.parseInt(parts[2]) + 1;
                }
            } catch (NumberFormatException e) {
                nextSeq = 1;
            }
        }

        return String.format("QT-%s-%03d", today, nextSeq);
    }

    @Override
    @Transactional
    public void createQuotation(QuotationDTO dto) {
        // 1) PK 생성 및 설정
        String newNo = generateNextQuotationNo();
        dto.setQuotationNo(newNo);
        dto.setStatus("ACTIVE"); // 기본적으로 활성 상태

        // 2) 메인 테이블에 삽입
        quotationMapper.insertQuotation(dto);

        // 3) 디테일 테이블에 삽입
        List<QuotationDetailDTO> details = dto.getDetails();
        if (details != null) {
            for (int i = 0; i < details.size(); i++) {
                QuotationDetailDTO d = details.get(i);
                d.setQuotationNo(newNo);
                d.setLineNo(i + 1);
                quotationMapper.insertQuotationDetail(d);
            }
        }
    }

    @Override
    public List<QuotationDTO> getQuotationList() {
        return quotationMapper.getQuotationList();
    }

    @Override
    public List<QuotationDetailDTO> getQuotationDetails(String quotationNo) {
        return quotationMapper.selectQuotationDetails(quotationNo);
    }

    // ===== 휴지통 관련 기능 구현 =====
    
    @Override
    public List<QuotationDTO> getArchivedQuotationList() {
        log.info("휴지통 견적서 목록 조회");
        return quotationMapper.getArchivedQuotationList();
    }
    
    @Override
    @Transactional
    public int archiveExpiredQuotations() {
        log.info("만료된 견적서 자동 아카이브 실행");
        int archivedCount = quotationMapper.archiveExpiredQuotations();
        log.info("{}건의 견적서가 자동으로 아카이브되었습니다.", archivedCount);
        return archivedCount;
    }
    
    @Override
    @Transactional
    public boolean moveToTrash(String quotationNo) {
        log.info("견적서 휴지통 이동: {}", quotationNo);
        int result = quotationMapper.moveToTrash(quotationNo);
        return result > 0;
    }
    
    @Override
    @Transactional
    public boolean restoreQuotation(String quotationNo) {
        log.info("견적서 복원: {}", quotationNo);
        int result = quotationMapper.restoreQuotation(quotationNo);
        return result > 0;
    }
    
    @Override
    @Transactional
    public boolean deleteQuotationPermanently(String quotationNo) {
        log.info("견적서 완전 삭제: {}", quotationNo);
        int result = quotationMapper.deleteQuotationPermanently(quotationNo);
        return result > 0;
    }
    
    @Override
    public Map<String, Object> getTrashStatistics() {
        List<QuotationDTO> archivedList = getArchivedQuotationList();
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalCount", archivedList.size());
        stats.put("thisMonthCount", archivedList.stream()
            .mapToInt(q -> {
                if (q.getArchivedAt() == null) return 0;
                LocalDate archivedDate = LocalDate.ofInstant(
                    q.getArchivedAt().toInstant(), 
                    java.time.ZoneId.systemDefault()
                );
                LocalDate thisMonth = LocalDate.now().withDayOfMonth(1);
                return archivedDate.isAfter(thisMonth.minusDays(1)) ? 1 : 0;
            })
            .sum());
        
        return stats;
    }
    
    /**
     * 매일 자정에 만료된 견적서를 자동으로 아카이브
     * 크론 표현식: 초 분 시 일 월 요일
     */
    @Scheduled(cron = "0 0 0 * * *") // 매일 자정
    public void scheduledArchiveExpiredQuotations() {
        try {
            int archivedCount = archiveExpiredQuotations();
            if (archivedCount > 0) {
                log.info("스케줄러: {}건의 만료된 견적서가 자동 아카이브되었습니다.", archivedCount);
            }
        } catch (Exception e) {
            log.error("스케줄러: 자동 아카이브 실행 중 오류 발생", e);
        }
    }
}