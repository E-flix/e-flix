package com.eflix.acc.service.impl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.eflix.acc.dto.BalanceSheetDTO;
import com.eflix.acc.mapper.BalanceSheetMapper;
import com.eflix.acc.service.BalanceSheetService;
import com.eflix.acc.template.BalanceSheetTemplate;
import com.eflix.common.security.auth.AuthUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/* ============================================
  - 작성자   : 김희정
  - 최초작성 : 2025-07-05
  - 설명     : 재무상태표 Service 구현체
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-07-05 (김희정): Service 구현체 생성
  - 2025-07-05 (김희정): 표준 템플릿 적용으로 간소화
=============================================== */
@Slf4j
@Service
@RequiredArgsConstructor
public class BalanceSheetServiceImpl implements BalanceSheetService {
    
    private final BalanceSheetMapper balanceSheetMapper;
    private final BalanceSheetTemplate balanceSheetTemplate;
    
    @Override
    public List<BalanceSheetDTO> getBalanceSheetByYear(Map<String, Object> params) {
        try {
            // 현재 년도 설정
            String currentYear = String.valueOf(LocalDate.now().getYear());
            params.put("year", currentYear);
            log.info("현재 년도로 설정: {}", currentYear);
            
            // AuthUtil에서 coIdx 가져오기
            String coIdx = AuthUtil.getCoIdx();
            params.put("coIdx", coIdx);
            log.info("AuthUtil에서 coIdx 가져옴: {}", coIdx);
            
            log.info("재무상태표 데이터 조회 시작 - params: {}", params);
            List<BalanceSheetDTO> result = balanceSheetMapper.getBalanceSheetByYear(params);
            log.info("재무상태표 데이터 조회 완료 - 조회건수: {}", result.size());
            return result;
        } catch (Exception e) {
            log.error("재무상태표 데이터 조회 중 오류 발생", e);
            throw new RuntimeException("재무상태표 데이터 조회 실패", e);
        }
    }
    
    @Override
    public List<Map<String, Object>> convertToGridFormat(List<BalanceSheetDTO> dbData) {
        try {
            List<Map<String, Object>> gridData = balanceSheetTemplate.getStandardTemplate();
            
            if (dbData != null && !dbData.isEmpty()) {
                log.info("DB 데이터 변환 시작 - 총 {}건", dbData.size());
                
                // DB 데이터를 표준계정과목명별로 그룹화
                Map<String, Long> accountAmountMap = new HashMap<>();
                Map<String, BalanceSheetDTO> accountInfoMap = new HashMap<>();
                
                for (BalanceSheetDTO dto : dbData) {
                    if (dto.getStandardAccountName() != null && dto.getSumAmount() != null) {
                        accountAmountMap.merge(dto.getStandardAccountName(), dto.getSumAmount(), Long::sum);
                        accountInfoMap.put(dto.getStandardAccountName(), dto);
                        
                        log.info("DB 데이터: {} = {}", dto.getStandardAccountName(), dto.getSumAmount());
                    }
                }
                
                // 템플릿에 금액 매칭 - matchKey 사용
                for (Map<String, Object> row : gridData) {
                    String matchKey = (String) row.get("matchKey");
                    
                    if (matchKey != null && !matchKey.isEmpty()) {
                        Long amount = findMatchByKey(matchKey, accountAmountMap);
                        if (amount != null && amount != 0) {
                            row.put("amount", formatAmount(amount));
                            log.info("매칭 성공: {} = {}", matchKey, formatAmount(amount));
                        }
                    }
                }
                
                // 합계 계산
                calculateTotals(gridData, accountAmountMap, accountInfoMap);
            }
            
            return gridData;
            
        } catch (Exception e) {
            log.error("그리드 형식 변환 중 오류 발생", e);
            return balanceSheetTemplate.getStandardTemplate();
        }
    }
    
    /**
     * 매칭키로 안전한 매칭
     */
    private Long findMatchByKey(String matchKey, Map<String, Long> accountAmountMap) {
        if (matchKey == null || matchKey.isEmpty()) {
            return null;
        }
        
        // 1. 정확한 매칭 (가장 우선)
        Long exactMatch = accountAmountMap.get(matchKey);
        if (exactMatch != null) {
            return exactMatch;
        }
        
        // 2. 최소한의 정제 후 매칭 (공백, 특수문자만)
        String cleanKey = simpleClean(matchKey);
        for (Map.Entry<String, Long> entry : accountAmountMap.entrySet()) {
            if (cleanKey.equals(simpleClean(entry.getKey()))) {
                log.info("정제 매칭: {} -> {} = {}", matchKey, entry.getKey(), entry.getValue());
                return entry.getValue();
            }
        }
        
        return null;
    }
    
    /**
     * 최소한의 정제 (공백과 기본 특수문자만)
     */
    private String simpleClean(String text) {
        if (text == null) return "";
        return text.trim()
                  .replaceAll("\\s+", "")     // 공백 제거
                  .replaceAll("및", "")        // "및" 제거
                  .replaceAll("&", "");        // "&" 제거
    }
    
    /**
     * 금액 포맷팅 (천단위 구분자 추가)
     */
    private String formatAmount(Long amount) {
        if (amount == null || amount == 0) return "";
        return String.format("%,d", amount);
    }
    
    /**
     * 합계 계산
     */
    private void calculateTotals(List<Map<String, Object>> gridData, 
                               Map<String, Long> accountAmountMap, 
                               Map<String, BalanceSheetDTO> accountInfoMap) {
        
        long assetTotal = 0;
        long liabilityTotal = 0;
        long equityTotal = 0;
        
        // 대분류별 합계 계산
        for (Map.Entry<String, BalanceSheetDTO> entry : accountInfoMap.entrySet()) {
            BalanceSheetDTO dto = entry.getValue();
            Long amount = accountAmountMap.get(entry.getKey());
            
            if (amount != null && dto.getMajorCategory() != null) {
                String majorCategory = dto.getMajorCategory();
                
                if ("자산".equals(majorCategory)) {
                    assetTotal += amount;
                } else if ("부채".equals(majorCategory)) {
                    liabilityTotal += amount;
                } else if ("자본".equals(majorCategory)) {
                    equityTotal += amount;
                }
            }
        }
        
        log.info("합계 계산 - 자산: {}, 부채: {}, 자본: {}", 
                formatAmount(assetTotal), formatAmount(liabilityTotal), formatAmount(equityTotal));
        
        // 총계 행에 금액 설정
        for (Map<String, Object> row : gridData) {
            String accountName = (String) row.get("accountName");
            if (accountName != null) {
                if (accountName.contains("자산총계")) {
                    row.put("amount", formatAmount(assetTotal));
                    log.info("자산총계 설정: {}", formatAmount(assetTotal));
                } else if (accountName.contains("부채총계")) {
                    row.put("amount", formatAmount(liabilityTotal));
                    log.info("부채총계 설정: {}", formatAmount(liabilityTotal));
                } else if (accountName.contains("자본총계")) {
                    row.put("amount", formatAmount(equityTotal));
                    log.info("자본총계 설정: {}", formatAmount(equityTotal));
                } else if (accountName.contains("부채와 자본총계")) {
                    row.put("amount", formatAmount(liabilityTotal + equityTotal));
                    log.info("부채와 자본총계 설정: {}", formatAmount(liabilityTotal + equityTotal));
                }
            }
        }
    }
}
