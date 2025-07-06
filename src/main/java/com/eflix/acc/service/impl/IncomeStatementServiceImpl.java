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
  - 2025-07-05 (김희정): 표준 템플릿 적용으로 간소화
  - 2025-07-06 (김희정): 중복 메서드 정리 및 손익계산 로직 통합
  - 2025-07-07 (김희정): 오류 처리 개선 및 누락 메서드 추가
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
                
                // 합계 계산 (오류 시 예외 전파)
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
     * 손익 계산 및 설정 (통합된 단일 메서드)
     */
    private void calculateProfitAndLoss(List<Map<String, Object>> gridData) {
        try {
            // 1. 상품매출원가 세부 계산
            calculateCostOfGoodsSold(gridData);
            
            // 2. 제조원가 세부 계산
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
            
            // 5. 손익 계산
            long grossProfit = totalSales - totalCogs; // 매출총이익
            long operatingProfit = grossProfit - totalSga; // 영업손익
            long netIncome = operatingProfit + totalNonOpIncome - totalNonOpExpense; // 당기순손익
            
            // 6. 손익 설정
            setProfitAndLossTotal(gridData, "Ⅲ. 매출총이익 ( Ⅰ-Ⅱ )", grossProfit);
            setProfitAndLossTotal(gridData, "Ⅴ. 영업손익( Ⅲ－Ⅳ )", operatingProfit);
            setProfitAndLossTotal(gridData, "Ⅷ. 당기순손익( Ⅴ＋Ⅵ－Ⅶ )", netIncome);
            
            log.info("손익계산 완료 - 매출액:{}, 매출원가:{}, 매출총이익:{}, 당기순손익:{}", 
                     formatAmount(totalSales), formatAmount(totalCogs), formatAmount(grossProfit), formatAmount(netIncome));
                     
        } catch (Exception e) {
            log.error("손익계산 중 심각한 오류 발생", e);
            throw new RuntimeException("손익계산서 계산 중 오류가 발생했습니다. 데이터를 확인해주세요: " + e.getMessage(), e);
        }
    }
    
    /**
     * 상품매출원가 계산 로직 설명
     * 
     * 1. 기초재고액 = 전년도 상품계정 잔액 (차변-대변)
     * 2. 당기매입액 = 상품계정 잔액 (차변-대변) + 기말재고액
     * 3. 기말재고액 = 상품매출원가 전표에서 상품계정 대변금액
     * 4. 타계정대체액 = 상품계정에서 다른 계정으로 대체된 금액 (대변)
     * 
     * 상품매출원가 = 기초재고액 + 당기매입액 - 기말재고액 - 타계정대체액
     */
    private void calculateCostOfGoodsSold(List<Map<String, Object>> gridData) {
        try {
            log.info("=== 상품매출원가 계산 시작 ===");
            
            // 1. 기초재고액 = 전년도 상품계정 잔액 (차변-대변)
            long beginningInventory = getBeginningInventory();
            log.info("1. 기초재고액: {}", formatAmount(beginningInventory));
            
            // 2. 기말재고액 = 상품매출원가 전표에서 상품계정 대변금액
            long endingInventory = getEndingInventory();
            log.info("2. 기말재고액: {}", formatAmount(endingInventory));
            
            // 3. 당기매입액 = 상품계정 잔액 (차변-대변) + 기말재고액
            long currentPurchases = getCurrentPurchases();
            log.info("3. 당기매입액: {}", formatAmount(currentPurchases));
            
            // 4. 타계정대체액 = 상품계정에서 다른 계정으로 대체된 금액 (대변)
            long otherAccountTransfer = getOtherAccountTransfer();
            log.info("4. 타계정대체액: {}", formatAmount(otherAccountTransfer));
            
            // 5. 상품매출원가 계산 = 기초재고액 + 당기매입액 - 기말재고액 - 타계정대체액
            long costOfGoodsSold = beginningInventory + currentPurchases - endingInventory - otherAccountTransfer;
            log.info("5. 상품매출원가 계산: {} + {} - {} - {} = {}", 
                    formatAmount(beginningInventory), formatAmount(currentPurchases), 
                    formatAmount(endingInventory), formatAmount(otherAccountTransfer), formatAmount(costOfGoodsSold));
            
            // 6. 각 항목별 금액 설정
            log.info("=== 템플릿에 금액 설정 ===");
            setAmountByCode(gridData, "11", beginningInventory); // 기초재고액
            log.info("코드 11(기초재고액) 설정: {}", formatAmount(beginningInventory));
            
            setAmountByCode(gridData, "12", currentPurchases);   // 당기매입액
            log.info("코드 12(당기매입액) 설정: {}", formatAmount(currentPurchases));
            
            setAmountByCode(gridData, "13", endingInventory);    // 기말재고액
            log.info("코드 13(기말재고액) 설정: {}", formatAmount(endingInventory));
            
            setAmountByCode(gridData, "14", otherAccountTransfer); // 타계정대체액
            log.info("코드 14(타계정대체액) 설정: {}", formatAmount(otherAccountTransfer));
            
            setAmountByCode(gridData, "10", costOfGoodsSold);    // 상품매출원가
            log.info("코드 10(상품매출원가) 설정: {}", formatAmount(costOfGoodsSold));
            
            log.info("=== 상품매출원가 계산 완료 ===");
                    
        } catch (Exception e) {
            log.error("상품매출원가 계산 중 예상치 못한 오류 발생", e);
            
            // 예상치 못한 오류시에는 모두 0으로 설정
            setAmountByCode(gridData, "11", 0L); // 기초재고액
            setAmountByCode(gridData, "12", 0L); // 당기매입액
            setAmountByCode(gridData, "13", 0L); // 기말재고액
            setAmountByCode(gridData, "14", 0L); // 타계정대체액
            setAmountByCode(gridData, "10", 0L); // 상품매출원가
            
            log.warn("상품매출원가 계산 오류로 인해 모든 값을 0으로 설정했습니다.");
        }
    }
    
    /**
     * 제조원가 계산 (제조ㆍ공사ㆍ분양ㆍ기타원가)
     */
    private void calculateManufacturingCost(List<Map<String, Object>> gridData) {
        try {
            // 제조원가도 상품매출원가와 동일한 구조로 계산 (현재는 모두 0)
            long mfgBeginningInventory = getMfgBeginningInventory();
            long mfgCurrentCost = getMfgCurrentCost();
            long mfgEndingInventory = getMfgEndingInventory();
            long mfgOtherAccountTransfer = getMfgOtherAccountTransfer();
            
            long manufacturingCost = mfgBeginningInventory + mfgCurrentCost - mfgEndingInventory - mfgOtherAccountTransfer;
            
            // 각 항목별 금액 설정 (0 값도 설정)
            setAmountByCode(gridData, "16", mfgBeginningInventory); // 제조 기초재고액
            setAmountByCode(gridData, "17", mfgCurrentCost);        // 제조 당기매입액
            setAmountByCode(gridData, "18", mfgEndingInventory);    // 제조 기말재고액
            setAmountByCode(gridData, "19", mfgOtherAccountTransfer); // 제조 타계정대체액
            setAmountByCode(gridData, "15", manufacturingCost);     // 제조ㆍ공사ㆍ분양ㆍ기타원가
            
            log.info("제조원가 계산 완료 - 제조원가:{}", formatAmount(manufacturingCost));
            
        } catch (Exception e) {
            log.error("제조원가 계산 중 예상치 못한 오류 발생", e);
            
            // 예상치 못한 오류시에는 모두 0으로 설정
            setAmountByCode(gridData, "16", 0L); // 제조 기초재고액
            setAmountByCode(gridData, "17", 0L); // 제조 당기원가
            setAmountByCode(gridData, "18", 0L); // 제조 기말재고액
            setAmountByCode(gridData, "19", 0L); // 제조 타계정대체액
            setAmountByCode(gridData, "15", 0L); // 제조원가
            
            log.warn("제조원가 계산 오류로 인해 모든 값을 0으로 설정했습니다.");
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
                        // 쉼표 제거 후 숫자 변환
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
                log.debug("손익항목 설정 - {}: {}", subsectionName, formatAmount(amount));
                break;
            }
        }
    }
    
    /**
     * 코드별 금액 설정 - 디버깅 강화
     */
    private void setAmountByCode(List<Map<String, Object>> gridData, String code, long amount) {
        boolean found = false;
        for (Map<String, Object> row : gridData) {
            String accountCode = (String) row.get("accountCode");
            if (code.equals(accountCode)) {
                row.put("amount", formatAmount(amount));
                log.debug("코드 {} 금액 설정 완료: {} -> {}", code, amount, formatAmount(amount));
                found = true;
                break;
            }
        }
        if (!found) {
            log.warn("코드 {}에 해당하는 행을 찾을 수 없습니다. 금액: {}", code, formatAmount(amount));
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
     * 금액 포맷팅 (Long용)
     */
    private String formatAmount(Long amount) {
        if (amount == null) return "0";
        return String.format("%,d", amount);
    }
    
    /**
     * 금액 포맷팅 (long용)
     */
    private String formatAmount(long amount) {
        return String.format("%,d", amount);
    }
    
    // 실제 DB 조회 메서드들
    
    /**
     * 상품 기초재고액 조회 (전년도 기말재고액)
     */
    private long getBeginningInventory() {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("coIdx", AuthUtil.getCoIdx());
            String previousYear = this.currentYear != null ? 
                String.valueOf(Integer.parseInt(this.currentYear) - 1) : 
                String.valueOf(LocalDate.now().getYear() - 1);
            params.put("previousYear", previousYear);
            
            Long result = incomeStatementMapper.getGoodsBeginningInventory(params);
            long amount = result != null ? result : 0L;
            
            log.debug("상품 기초재고액 조회 완료: {}", formatAmount(amount));
            return amount;
            
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
            params.put("year", this.currentYear != null ? this.currentYear : String.valueOf(LocalDate.now().getYear()));
            params.put("endMonth", this.currentEndMonth != null ? this.currentEndMonth : "12");
            
            log.info("상품 기말재고액 조회 파라미터 - coIdx: {}, year: {}, endMonth: {}", 
                    params.get("coIdx"), params.get("year"), params.get("endMonth"));
            
            Long result = incomeStatementMapper.getGoodsEndingInventory(params);
            long amount = result != null ? result : 0L;
            
            log.info("상품 기말재고액 조회 결과: {} (raw: {})", formatAmount(amount), result);
            return amount;
            
        } catch (Exception e) {
            log.error("상품 기말재고액 조회 중 오류 발생", e);
            return 0L;
        }
    }
    
    /**
     * 상품 당기매입액 조회 (상품계정 잔액 + 기말재고액) - 디버깅 강화
     */
    private long getCurrentPurchases() {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("coIdx", AuthUtil.getCoIdx());
            params.put("year", this.currentYear != null ? this.currentYear : String.valueOf(LocalDate.now().getYear()));
            params.put("endMonth", this.currentEndMonth != null ? this.currentEndMonth : "12");
            
            log.info("상품 당기매입액 조회 파라미터 - coIdx: {}, year: {}, endMonth: {}", 
                    params.get("coIdx"), params.get("year"), params.get("endMonth"));
            
            Long result = incomeStatementMapper.getGoodsPurchaseAmount(params);
            long amount = result != null ? result : 0L;
            
            log.info("상품 당기매입액 조회 결과: {} (raw: {})", formatAmount(amount), result);
            return amount;
            
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
            params.put("year", this.currentYear != null ? this.currentYear : String.valueOf(LocalDate.now().getYear()));
            params.put("endMonth", this.currentEndMonth != null ? this.currentEndMonth : "12");
            
            Long result = incomeStatementMapper.getGoodsTransferAmount(params);
            long amount = result != null ? result : 0L;
            
            log.debug("상품 타계정대체액 조회 완료: {}", formatAmount(amount));
            return amount;
            
        } catch (Exception e) {
            log.error("상품 타계정대체액 조회 중 오류 발생", e);
            return 0L;
        }
    }
    
    /**
     * 제조원가 기초재고액 조회
     */
    private long getMfgBeginningInventory() {
        // TODO: 제조관련 전년도 기말재고액 조회 구현 필요
        log.debug("제조원가 기초재고액 조회 기능이 아직 구현되지 않아 0으로 처리합니다.");
        return 0L;
    }
    
    /**
     * 제조원가 당기원가 조회
     */
    private long getMfgCurrentCost() {
        // TODO: 당기 제조원가 차변 합계 조회 구현 필요
        log.debug("제조원가 당기원가 조회 기능이 아직 구현되지 않아 0으로 처리합니다.");
        return 0L;
    }
    
    /**
     * 제조원가 기말재고액 조회
     */
    private long getMfgEndingInventory() {
        // TODO: 제조관련 현재 잔액 조회 구현 필요
        log.debug("제조원가 기말재고액 조회 기능이 아직 구현되지 않아 0으로 처리합니다.");
        return 0L;
    }
    
    /**
     * 제조원가 타계정대체액 조회
     */
    private long getMfgOtherAccountTransfer() {
        // TODO: 제조원가 타계정대체액 조회 구현 필요
        log.debug("제조원가 타계정대체액 조회 기능이 아직 구현되지 않아 0으로 처리합니다.");
        return 0L;
    }
}
