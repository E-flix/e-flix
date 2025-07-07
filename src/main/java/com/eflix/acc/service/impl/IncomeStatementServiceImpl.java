package com.eflix.acc.service.impl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.eflix.acc.dto.IncomeStatementDTO;
import com.eflix.acc.mapper.IncomeStatementMapper;
import com.eflix.acc.service.IncomeStatementService;
import com.eflix.acc.template.IncomeStatementTemplate;
import com.eflix.common.security.auth.AuthUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/* ============================================
  - 작성자   : 김희정
  - 최초작성 : 2025-07-05
  - 설명     : 손익계산서 Service 구현체
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-07-05 (김희정): Service 구현체 생성
=============================================== */
@Slf4j
@Service
@RequiredArgsConstructor
public class IncomeStatementServiceImpl implements IncomeStatementService {
    
    private final IncomeStatementMapper incomeStatementMapper;
    private final IncomeStatementTemplate incomeStatementTemplate;
    
    // 조회 파라미터를 저장할 필드
    private String currentYear;
    private String currentEndMonth;
    
    @Override
    public List<IncomeStatementDTO> getIncomeStatementByYear(Map<String, Object> params) {
        try {
            // 현재 년도 설정
            this.currentYear = String.valueOf(LocalDate.now().getYear());
            params.put("year", this.currentYear);
            
            // endMonth 기본값 설정
            this.currentEndMonth = (String) params.get("endMonth");
            if (this.currentEndMonth == null || this.currentEndMonth.isEmpty()) {
                this.currentEndMonth = "12";
                params.put("endMonth", this.currentEndMonth);
            }
            
            // AuthUtil에서 coIdx 가져오기
            String coIdx = AuthUtil.getCoIdx();
            params.put("coIdx", coIdx);
            
            log.info("손익계산서 데이터 조회 - year: {}, endMonth: {}까지, coIdx: {}", this.currentYear, this.currentEndMonth, coIdx);
            List<IncomeStatementDTO> result = incomeStatementMapper.getIncomeStatementByYear(params);
            log.info("손익계산서 데이터 조회 완료 - 조회건수: {}", result.size());
            return result;
        } catch (Exception e) {
            log.error("손익계산서 데이터 조회 중 오류 발생", e);
            throw new RuntimeException("손익계산서 데이터 조회 실패", e);
        }
    }
    
    @Override
    public List<Map<String, Object>> convertToGridFormat(List<IncomeStatementDTO> dbData) {
        try {
            // 기존 템플릿 그대로 사용 (99개 전체 항목)
            List<Map<String, Object>> gridData = incomeStatementTemplate.getStandardTemplate();
            
            if (dbData != null && !dbData.isEmpty()) {
                // DB 데이터를 표준계정과목명별로 그룹화
                Map<String, Long> accountAmountMap = new HashMap<>();
                for (IncomeStatementDTO dto : dbData) {
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
                calculateProfitAndLoss(gridData);
            } else {
                log.warn("손익계산서 DB 데이터가 없습니다. 빈 템플릿을 반환합니다.");
            }
            
            return gridData;
            
        } catch (Exception e) {
            log.error("그리드 형식 변환 중 오류 발생", e);
            throw new RuntimeException("손익계산서 데이터 변환 실패: " + e.getMessage(), e);
        }
    }
    
    /**
     * 손익 계산 및 설정
     */
    private void calculateProfitAndLoss(List<Map<String, Object>> gridData) {
        try {
            // 1. 상품매출원가 세부 계산
            calculateCostOfGoodsSold(gridData);
            
            // 2. 제조원가 세부 계산 (일단 0으로 처리하되 템플릿 항목은 유지)
            calculateManufacturingCost(gridData);
            
            // 3. 대분류 합계 계산
            long totalSales = calculateSectionTotal(gridData, "02", "08"); // 제품매출~기타매출
            long totalCogs = calculateCostOfGoodsSoldTotal(gridData); // 상품매출원가 + 제조원가
            long totalSga = calculateSectionTotal(gridData, "22", "61"); // 급여~기타
            long totalNonOpIncome = calculateSectionTotal(gridData, "64", "80"); // 이자수익~기타
            long totalNonOpExpense = calculateSectionTotal(gridData, "82", "98"); // 이자비용~기타
            
            // 4. 대분류 합계 설정
            setProfitAndLossTotal(gridData, "Ⅰ. 매출액", totalSales);
            setProfitAndLossTotal(gridData, "Ⅱ. 매출원가", totalCogs);
            setProfitAndLossTotal(gridData, "Ⅳ. 판매비와관리비", totalSga);
            setProfitAndLossTotal(gridData, "Ⅵ. 영업외수익", totalNonOpIncome);
            setProfitAndLossTotal(gridData, "Ⅶ. 영업외비용", totalNonOpExpense);
            
            // 5. 손익 계산 및 설정
            long grossProfit = totalSales - totalCogs;
            long operatingProfit = grossProfit - totalSga;
            long netIncome = operatingProfit + totalNonOpIncome - totalNonOpExpense;
            
            setProfitAndLossTotal(gridData, "Ⅲ. 매출총이익 ( Ⅰ-Ⅱ )", grossProfit);
            setProfitAndLossTotal(gridData, "Ⅴ. 영업손익( Ⅲ－Ⅳ )", operatingProfit);
            setProfitAndLossTotal(gridData, "Ⅷ. 당기순손익( Ⅴ＋Ⅵ－Ⅶ )", netIncome);
            
            log.info("손익계산 완료 - 매출액:{}, 매출원가:{}, 매출총이익:{}, 당기순손익:{}", 
                     formatAmount(totalSales), formatAmount(totalCogs), formatAmount(grossProfit), formatAmount(netIncome));
                     
        } catch (Exception e) {
            log.error("손익계산 중 오류 발생", e);
            throw new RuntimeException("손익계산서 계산 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }
    
    /**
     * 상품매출원가 계산
     */
    private void calculateCostOfGoodsSold(List<Map<String, Object>> gridData) {
        try {
            long beginningInventory = getBeginningInventory();
            long endingInventory = getEndingInventory();
            long currentPurchases = getCurrentPurchases();
            long otherAccountTransfer = getOtherAccountTransfer();
            
            long costOfGoodsSold = beginningInventory + currentPurchases - endingInventory - otherAccountTransfer;
            
            setAmountByCode(gridData, "11", beginningInventory);
            setAmountByCode(gridData, "12", currentPurchases);
            setAmountByCode(gridData, "13", endingInventory);
            setAmountByCode(gridData, "14", otherAccountTransfer);
            setAmountByCode(gridData, "10", costOfGoodsSold);
            
            log.info("상품매출원가 계산 완료 - 기초:{}, 매입:{}, 기말:{}, 대체:{}, 매출원가:{}", 
                    formatAmount(beginningInventory), formatAmount(currentPurchases), 
                    formatAmount(endingInventory), formatAmount(otherAccountTransfer), formatAmount(costOfGoodsSold));
                    
        } catch (Exception e) {
            log.error("상품매출원가 계산 중 오류 발생", e);
            setAmountByCode(gridData, "11", 0L);
            setAmountByCode(gridData, "12", 0L);
            setAmountByCode(gridData, "13", 0L);
            setAmountByCode(gridData, "14", 0L);
            setAmountByCode(gridData, "10", 0L);
        }
    }
    
    /**
     * 제조원가 계산 (템플릿 항목 유지를 위해 0으로 설정)
     */
    private void calculateManufacturingCost(List<Map<String, Object>> gridData) {
        try {
            // 제조원가는 현재 구현되지 않았으므로 0으로 설정하되 템플릿 항목은 유지
            setAmountByCode(gridData, "16", 0L); // 제조 기초재고액
            setAmountByCode(gridData, "17", 0L); // 제조 당기매입액
            setAmountByCode(gridData, "18", 0L); // 제조 기말재고액
            setAmountByCode(gridData, "19", 0L); // 제조 타계정대체액
            setAmountByCode(gridData, "15", 0L); // 제조ㆍ공사ㆍ분양ㆍ기타원가
            
            log.debug("제조원가 항목들을 0으로 설정했습니다 (템플릿 구조 유지)");
            
        } catch (Exception e) {
            log.error("제조원가 설정 중 오류 발생", e);
        }
    }
    
    /**
     * 매출원가 총계 계산 (상품매출원가 + 제조원가)
     */
    private long calculateCostOfGoodsSoldTotal(List<Map<String, Object>> gridData) {
        long costOfGoodsSold = getAmountByCode(gridData, "10"); // 상품매출원가
        long manufacturingCost = getAmountByCode(gridData, "15"); // 제조원가
        return costOfGoodsSold + manufacturingCost;
    }
    
    /**
     * 구간별 합계 계산
     */
    private long calculateSectionTotal(List<Map<String, Object>> gridData, String startCode, String endCode) {
        long total = 0L;
        int start = Integer.parseInt(startCode);
        int end = Integer.parseInt(endCode);
        
        for (Map<String, Object> row : gridData) {
            String accountCode = (String) row.get("accountCode");
            String amountStr = (String) row.get("amount");
            
            if (accountCode != null && !accountCode.isEmpty() && amountStr != null && !amountStr.isEmpty()) {
                try {
                    int code = Integer.parseInt(accountCode);
                    if (code >= start && code <= end) {
                        String cleanAmount = amountStr.replace(",", "");
                        long amount = Long.parseLong(cleanAmount);
                        total += amount;
                    }
                } catch (NumberFormatException e) {
                    log.warn("금액 변환 실패 - 코드:{}, 금액:{}", accountCode, amountStr);
                }
            }
        }
        
        return total;
    }
    
    /**
     * 손익 항목에 금액 설정
     */
    private void setProfitAndLossTotal(List<Map<String, Object>> gridData, String subsectionName, long amount) {
        for (Map<String, Object> row : gridData) {
            String subsection = (String) row.get("subsection");
            if (subsection != null && subsection.equals(subsectionName)) {
                row.put("total", formatAmount(amount));
                break;
            }
        }
    }
    
    /**
     * 코드별 금액 설정
     */
    private void setAmountByCode(List<Map<String, Object>> gridData, String code, long amount) {
        for (Map<String, Object> row : gridData) {
            String accountCode = (String) row.get("accountCode");
            if (code.equals(accountCode)) {
                row.put("amount", formatAmount(amount));
                break;
            }
        }
    }
    
    /**
     * 코드별 금액 조회
     */
    private long getAmountByCode(List<Map<String, Object>> gridData, String code) {
        for (Map<String, Object> row : gridData) {
            String accountCode = (String) row.get("accountCode");
            if (code.equals(accountCode)) {
                String amountStr = (String) row.get("amount");
                if (amountStr != null && !amountStr.isEmpty()) {
                    try {
                        return Long.parseLong(amountStr.replace(",", ""));
                    } catch (NumberFormatException e) {
                        log.warn("금액 변환 실패 - 코드:{}, 금액:{}", code, amountStr);
                    }
                }
                break;
            }
        }
        return 0L;
    }
    
    /**
     * 매칭키로 매칭
     */
    private Long findMatchByKey(String matchKey, Map<String, Long> accountAmountMap) {
        if (matchKey == null || matchKey.isEmpty()) {
            return null;
        }
        
        // 정확한 매칭
        Long exactMatch = accountAmountMap.get(matchKey);
        if (exactMatch != null) {
            return exactMatch;
        }
        
        // 정제 후 매칭
        String cleanKey = cleanText(matchKey);
        for (Map.Entry<String, Long> entry : accountAmountMap.entrySet()) {
            if (cleanKey.equals(cleanText(entry.getKey()))) {
                return entry.getValue();
            }
        }
        
        return null;
    }
    
    /**
     * 텍스트 정제
     */
    private String cleanText(String text) {
        if (text == null) return "";
        return text.trim().replaceAll("\\s+", "").replaceAll("및", "").replaceAll("&", "");
    }
    
    /**
     * 금액 포맷팅
     */
    private String formatAmount(long amount) {
        return String.format("%,d", amount);
    }
    
    // DB 조회 메서드들 (상품매출원가 관련만 실제 구현)
    
    /**
     * 상품 기초재고액 조회 (전년도 기말재고액)
     */
    private long getBeginningInventory() {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("coIdx", AuthUtil.getCoIdx());
            String previousYear = String.valueOf(Integer.parseInt(this.currentYear) - 1);
            params.put("year", previousYear);
            
            Long result = incomeStatementMapper.getGoodsBeginningInventory(params);
            return result != null ? result : 0L;
            
        } catch (Exception e) {
            log.error("상품 기초재고액 조회 중 오류 발생", e);
            return 0L;
        }
    }
    
    /**
     * 상품 기말재고액 조회 (상품매출원가 전표에서 상품계정 대변금액)
     */
    private long getEndingInventory() {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("coIdx", AuthUtil.getCoIdx());
            params.put("year", this.currentYear);
            params.put("endMonth", this.currentEndMonth);
            
            Long result = incomeStatementMapper.getGoodsEndingInventory(params);
            return result != null ? result : 0L;
            
        } catch (Exception e) {
            log.error("상품 기말재고액 조회 중 오류 발생", e);
            return 0L;
        }
    }
    
    /**
     * 상품 당기매입액 조회 (상품계정 잔액 + 기말재고액)
     */
    private long getCurrentPurchases() {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("coIdx", AuthUtil.getCoIdx());
            params.put("year", this.currentYear);
            params.put("endMonth", this.currentEndMonth);
            
            Long result = incomeStatementMapper.getGoodsPurchaseAmount(params);
            return result != null ? result : 0L;
            
        } catch (Exception e) {
            log.error("상품 당기매입액 조회 중 오류 발생", e);
            return 0L;
        }
    }
    
    /**
     * 상품 타계정대체액 조회
     */
    private long getOtherAccountTransfer() {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("coIdx", AuthUtil.getCoIdx());
            params.put("year", this.currentYear);
            params.put("endMonth", this.currentEndMonth);
            
            Long result = incomeStatementMapper.getGoodsTransferAmount(params);
            return result != null ? result : 0L;
            
        } catch (Exception e) {
            log.error("상품 타계정대체액 조회 중 오류 발생", e);
            return 0L;
        }
    }
}
