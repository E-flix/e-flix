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
            
            // AuthUtil에서 coIdx 가져오기
            String coIdx = AuthUtil.getCoIdx();
            params.put("coIdx", coIdx);
            
            log.info("재무상태표 데이터 조회 - year: {}, coIdx: {}", currentYear, coIdx);
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
                // DB 데이터를 표준계정과목명별로 그룹화
                Map<String, Long> accountAmountMap = new HashMap<>();
                
                for (BalanceSheetDTO dto : dbData) {
                    if (dto.getStandardAccountName() != null && dto.getSumAmount() != null) {
                        accountAmountMap.merge(dto.getStandardAccountName(), dto.getSumAmount(), Long::sum);
                    }
                }
                
                // 템플릿에 금액 매칭
                for (Map<String, Object> row : gridData) {
                    String matchKey = (String) row.get("matchKey");
                    if (matchKey != null && !matchKey.isEmpty()) {
                        Long amount = findMatchByKey(matchKey, accountAmountMap);
                        if (amount != null && amount != 0) {
                            row.put("amount", formatAmount(amount));
                        }
                    }
                }
                
                // 합계 계산
                calculateTotals(gridData);
            }
            
            return gridData;
            
        } catch (Exception e) {
            log.error("그리드 형식 변환 중 오류 발생", e);
            return balanceSheetTemplate.getStandardTemplate();
        }
    }
    
    /**
     * 매칭키로 매칭
     */
    private Long findMatchByKey(String matchKey, Map<String, Long> accountAmountMap) {
        if (matchKey == null || matchKey.isEmpty()) {
            return null;
        }
        
        // 1. 정확한 매칭
        Long exactMatch = accountAmountMap.get(matchKey);
        if (exactMatch != null) {
            return exactMatch;
        }
        
        // 2. 정제 후 매칭
        String cleanKey = cleanText(matchKey);
        for (Map.Entry<String, Long> entry : accountAmountMap.entrySet()) {
            if (cleanKey.equals(cleanText(entry.getKey()))) {
                return entry.getValue();
            }
        }
        
        return null;
    }
    
    /**
     * 텍스트 정제 (공백, 특수문자 제거)
     */
    private String cleanText(String text) {
        if (text == null) return "";
        return text.trim().replaceAll("\\s+", "").replaceAll("및", "").replaceAll("&", "");
    }
    
    /**
     * 금액 포맷팅
     */
    private String formatAmount(Long amount) {
        if (amount == null) return "";
        if (amount == 0) return "0";
        return String.format("%,d", amount);
    }
    
    /**
     * 합계 계산 (통합)
     */
    private void calculateTotals(List<Map<String, Object>> gridData) {
        // 1. 소분류 합계 계산
        calculateSubCategoryTotals(gridData);
        
        // 2. 중분류 합계 계산
        calculateMidCategoryTotals(gridData);
        
        // 3. 대분류 합계 계산
        calculateMainCategoryTotals(gridData);
        
        // 4. 최종 총계 계산
        calculateGrandTotals(gridData);
    }
    
    /**
     * 소분류 합계 계산
     */
    private void calculateSubCategoryTotals(List<Map<String, Object>> gridData) {
        String[] subCategoryNames = {
            "(4) 단기대여금", "(7) 미수금", "(3) 장기대여금", "1. 장기차입금"
        };
        
        for (String subCategory : subCategoryNames) {
            calculateSubCategoryTotal(gridData, subCategory);
        }
    }
    
    /**
     * 특정 소분류의 합계 계산
     */
    private void calculateSubCategoryTotal(List<Map<String, Object>> gridData, String subCategoryName) {
        long total = 0;
        boolean inSubCategory = false;
        int subCategoryIndex = -1;
        
        for (int i = 0; i < gridData.size(); i++) {
            Map<String, Object> row = gridData.get(i);
            String accountName = (String) row.get("accountName");
            String amount = (String) row.get("amount");
            
            // 소분류 시작 확인
            if (accountName != null && accountName.equals(subCategoryName)) {
                inSubCategory = true;
                subCategoryIndex = i;
                continue;
            }
            
            // 소분류 종료 조건
            if (inSubCategory && accountName != null && !accountName.isEmpty()) {
                if (!accountName.trim().matches("^[①②③④⑤⑥⑦⑧⑨⑩].*")) {
                    if (accountName.startsWith("(") || accountName.matches("^\\d+\\..*")) {
                        break;
                    }
                }
            }
            
            // 소분류 내의 하위 항목 금액 합계
            if (inSubCategory && amount != null && !amount.isEmpty() && 
                accountName != null && accountName.trim().matches("^[①②③④⑤⑥⑦⑧⑨⑩].*")) {
                try {
                    total += Long.parseLong(amount.replaceAll(",", ""));
                } catch (NumberFormatException e) {
                    // 무시
                }
            }
        }
        
        // 소분류 헤더에 합계 설정
        if (subCategoryIndex >= 0) {
            gridData.get(subCategoryIndex).put("total", formatAmount(total));
        }
    }
    
    /**
     * 중분류 합계 계산
     */
    private void calculateMidCategoryTotals(List<Map<String, Object>> gridData) {
        String[] midCategories = {
            "1. 당좌자산", "2. 재고자산", "1. 투자자산", 
            "2. 유형자산", "3. 무형자산", "4. 기타 비유동자산"
        };
        
        for (String category : midCategories) {
            calculateMidCategoryTotal(gridData, category);
        }
    }
    
    /**
     * 중분류 합계 계산
     */
    private void calculateMidCategoryTotal(List<Map<String, Object>> gridData, String categoryName) {
        long total = 0;
        boolean inCategory = false;
        int categoryIndex = -1;
        
        for (int i = 0; i < gridData.size(); i++) {
            Map<String, Object> row = gridData.get(i);
            String subsection = (String) row.get("subsection");
            String subcategory = (String) row.get("subcategory");
            String accountName = (String) row.get("accountName");
            String amount = (String) row.get("amount");
            
            // 중분류 시작
            if (subcategory != null && subcategory.equals(categoryName)) {
                inCategory = true;
                categoryIndex = i;
                continue;
            }
            
            // 종료 조건: 다른 중분류나 대분류가 시작되면 종료
            if (inCategory) {
                if (subcategory != null && !subcategory.isEmpty() && !subcategory.equals(categoryName)) {
                    break;
                }
                if (subsection != null && !subsection.isEmpty() &&
                    (subsection.startsWith("Ⅰ.") || subsection.startsWith("Ⅱ.") || 
                     subsection.startsWith("Ⅲ.") || subsection.startsWith("Ⅳ."))) {
                    break;
                }
            }
            
            // 중분류 내의 개별 항목들만 합산
            if (inCategory && accountName != null && !accountName.isEmpty()) {
                if (accountName.trim().matches("^\\(\\d+\\).*")) {
                    if (amount != null && !amount.isEmpty()) {
                        try {
                            long amountValue = Long.parseLong(amount.replaceAll(",", ""));
                            total += amountValue;
                        } catch (NumberFormatException e) {
                            // 무시
                        }
                    }
                }
            }
        }
        
        // 중분류 헤더에 합계 설정
        if (categoryIndex >= 0) {
            gridData.get(categoryIndex).put("total", formatAmount(total));
        }
    }
    
    /**
     * 대분류 합계 계산
     */
    private void calculateMainCategoryTotals(List<Map<String, Object>> gridData) {
        String[] mainCategories = {
            "Ⅰ. 유동자산", "Ⅱ. 비유동자산", "Ⅰ. 유동부채", "Ⅱ. 비유동부채",
            "Ⅲ. 자본금", "Ⅳ. 당기순손익"
        };
        
        for (String category : mainCategories) {
            long total = calculateCategoryTotal(gridData, category);
            setCategoryTotal(gridData, category, total);
        }
    }
    
    /**
     * 카테고리 합계 계산
     */
    private long calculateCategoryTotal(List<Map<String, Object>> gridData, String categoryName) {
        long total = 0;
        boolean inCategory = false;
        
        for (Map<String, Object> row : gridData) {
            String subsection = (String) row.get("subsection");
            String subcategory = (String) row.get("subcategory");
            String accountName = (String) row.get("accountName");
            String amount = (String) row.get("amount");
            String midTotal = (String) row.get("total");
            
            // 대분류 시작
            if (subsection != null && subsection.equals(categoryName)) {
                inCategory = true;
                continue;
            }
            
            // 다른 대분류 시작하면 종료
            if (inCategory && subsection != null && 
                (subsection.startsWith("Ⅰ.") || subsection.startsWith("Ⅱ.") || 
                 subsection.startsWith("Ⅲ.") || subsection.startsWith("Ⅳ.")) &&
                !subsection.equals(categoryName)) {
                break;
            }
            
            // 대분류 내 합계 계산
            if (inCategory) {
                // 자산 대분류인 경우: 중분류 total 값들만 합산
                if (categoryName.contains("자산")) {
                    if (subcategory != null && !subcategory.isEmpty() && 
                        midTotal != null && !midTotal.isEmpty()) {
                        try {
                            long midTotalValue = Long.parseLong(midTotal.replaceAll(",", ""));
                            total += midTotalValue;
                        } catch (NumberFormatException e) {
                            // 무시
                        }
                    }
                }
                // 부채나 자본 대분류인 경우: 개별 항목들 직접 합산
                else {
                    if ((subcategory == null || subcategory.isEmpty()) && 
                         amount != null && !amount.isEmpty() &&
                         accountName != null && !accountName.isEmpty() &&
                         accountName.trim().matches("^\\(\\d+\\).*")) {
                        try {
                            long amountValue = Long.parseLong(amount.replaceAll(",", ""));
                            total += amountValue;
                        } catch (NumberFormatException e) {
                            // 무시
                        }
                    }
                }
            }
        }
        
        return total;
    }
    
    /**
     * 카테고리 헤더에 합계 설정
     */
    private void setCategoryTotal(List<Map<String, Object>> gridData, String categoryName, long total) {
        for (Map<String, Object> row : gridData) {
            String subsection = (String) row.get("subsection");
            if (subsection != null && subsection.equals(categoryName)) {
                row.put("total", formatAmount(total));
                break;
            }
        }
    }
    
    /**
     * 최종 총계 계산
     */
    private void calculateGrandTotals(List<Map<String, Object>> gridData) {
        // 각 카테고리 합계 가져오기
        long currentAssetTotal = getCategoryTotal(gridData, "Ⅰ. 유동자산");
        long nonCurrentAssetTotal = getCategoryTotal(gridData, "Ⅱ. 비유동자산");
        long currentLiabilityTotal = getCategoryTotal(gridData, "Ⅰ. 유동부채");
        long nonCurrentLiabilityTotal = getCategoryTotal(gridData, "Ⅱ. 비유동부채");
        
        // 자본총계 계산
        long equityTotal = calculateEquityTotal(gridData);
        
        // 총계 계산
        long totalAsset = currentAssetTotal + nonCurrentAssetTotal;
        long totalLiability = currentLiabilityTotal + nonCurrentLiabilityTotal;
        long totalLiabilityAndEquity = totalLiability + equityTotal;
        
        // 총계 행에 설정
        setGrandTotal(gridData, "자산총계", totalAsset);
        setGrandTotal(gridData, "부채총계", totalLiability);
        setGrandTotal(gridData, "자본총계", equityTotal);
        setGrandTotal(gridData, "부채와 자본총계", totalLiabilityAndEquity);
    }
    
    /**
     * 카테고리 total 값 가져오기
     */
    private long getCategoryTotal(List<Map<String, Object>> gridData, String categoryName) {
        for (Map<String, Object> row : gridData) {
            String subsection = (String) row.get("subsection");
            if (subsection != null && subsection.equals(categoryName)) {
                String total = (String) row.get("total");
                if (total != null && !total.isEmpty()) {
                    try {
                        return Long.parseLong(total.replaceAll(",", ""));
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                }
                return 0;
            }
        }
        return 0;
    }
    
    /**
     * 자본총계 계산
     */
    private long calculateEquityTotal(List<Map<String, Object>> gridData) {
        long capitalTotal = getCategoryTotal(gridData, "Ⅲ. 자본금");
        long currentEarningsTotal = getCategoryTotal(gridData, "Ⅳ. 당기순손익");
        return capitalTotal + currentEarningsTotal;
    }
    
    /**
     * 총계 행에 금액 설정
     */
    private void setGrandTotal(List<Map<String, Object>> gridData, String totalName, long amount) {
        for (Map<String, Object> row : gridData) {
            String accountName = (String) row.get("accountName");
            if (accountName != null && accountName.contains(totalName)) {
                row.put("total", formatAmount(amount));
                break;
            }
        }
    }
}
