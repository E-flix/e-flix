package com.eflix.acc.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.eflix.acc.dto.BalanceSheetDTO;
import com.eflix.acc.mapper.BalanceSheetMapper;
import com.eflix.acc.service.BalanceSheetService;
import com.eflix.common.security.auth.AuthUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/* ============================================
  - 작성자   : 김희정
  - 최초작성 : 2025-07-05
  - 설명     : IncomeStatementServiceImpl 구현
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-07-05 (김희정): IncomeStatementServiceImpl 생성
=============================================== */
@Slf4j
@Service
@RequiredArgsConstructor
public class BalanceSheetServiceImpl implements BalanceSheetService {
  private final BalanceSheetMapper balanceSheetMapper;

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
        List<Map<String, Object>> gridData = new ArrayList<>();
        
        try {
            // 대분류별로 그룹화
            Map<String, List<BalanceSheetDTO>> groupedData = dbData.stream()
                .collect(Collectors.groupingBy(BalanceSheetDTO::getMajorCategory));
            
            // 자산 섹션
            addAssetSection(gridData, groupedData.get("자산"));
            
            // 부채 섹션
            addLiabilitySection(gridData, groupedData.get("부채"));
            
            // 자본 섹션
            addEquitySection(gridData, groupedData.get("자본"));
            
            // 총계 섹션
            addTotalSection(gridData, groupedData);
            
        } catch (Exception e) {
            log.error("그리드 형식 변환 중 오류 발생", e);
            // 오류 시 기본 구조만 반환
            addDefaultStructure(gridData);
        }
        
        return gridData;
    }
    
    /**
     * 자산 섹션 추가
     */
    private void addAssetSection(List<Map<String, Object>> gridData, List<BalanceSheetDTO> assetData) {
        // 대분류 헤더
        gridData.add(createGridRow("자 산", "", "", "", "", "", "", true, 0, false));
        
        if (assetData != null && !assetData.isEmpty()) {
            long assetTotal = 0;
            
            for (BalanceSheetDTO asset : assetData) {
                gridData.add(createGridRow("", "", "", 
                    "", // accountCode는 DB에서 안가져오므로 빈값
                    asset.getStandardAccountName(),
                    formatAmount(asset.getSumAmount()), 
                    "", false, 3, false));
                assetTotal += (asset.getSumAmount() != null ? asset.getSumAmount() : 0);
            }
            
            // 자산총계
            gridData.add(createGridRow("", "", "", "", "자산총계", "", 
                formatAmount(assetTotal), false, 0, true));
        }
    }
    
    /**
     * 부채 섹션 추가
     */
    private void addLiabilitySection(List<Map<String, Object>> gridData, List<BalanceSheetDTO> liabilityData) {
        // 대분류 헤더
        gridData.add(createGridRow("부 채", "", "", "", "", "", "", true, 0, false));
        
        if (liabilityData != null && !liabilityData.isEmpty()) {
            long liabilityTotal = 0;
            
            for (BalanceSheetDTO liability : liabilityData) {
                gridData.add(createGridRow("", "", "", 
                    "", // accountCode는 DB에서 안가져오므로 빈값
                    liability.getStandardAccountName(),
                    formatAmount(liability.getSumAmount()), 
                    "", false, 3, false));
                liabilityTotal += (liability.getSumAmount() != null ? liability.getSumAmount() : 0);
            }
            
            // 부채총계
            gridData.add(createGridRow("", "", "", "", "부채총계", "", 
                formatAmount(liabilityTotal), false, 0, true));
        }
    }
    
    /**
     * 자본 섹션 추가
     */
    private void addEquitySection(List<Map<String, Object>> gridData, List<BalanceSheetDTO> equityData) {
        // 대분류 헤더
        gridData.add(createGridRow("자 본", "", "", "", "", "", "", true, 0, false));
        
        if (equityData != null && !equityData.isEmpty()) {
            long equityTotal = 0;
            
            for (BalanceSheetDTO equity : equityData) {
                gridData.add(createGridRow("", "", "", 
                    "", // accountCode는 DB에서 안가져오므로 빈값
                    equity.getStandardAccountName(),
                    formatAmount(equity.getSumAmount()), 
                    "", false, 3, false));
                equityTotal += (equity.getSumAmount() != null ? equity.getSumAmount() : 0);
            }
            
            // 자본총계
            gridData.add(createGridRow("", "", "", "", "자본총계", "", 
                formatAmount(equityTotal), false, 0, true));
        }
    }
    
    /**
     * 총계 섹션 추가
     */
    private void addTotalSection(List<Map<String, Object>> gridData, Map<String, List<BalanceSheetDTO>> groupedData) {
        long liabilityTotal = calculateTotal(groupedData.get("부채"));
        long equityTotal = calculateTotal(groupedData.get("자본"));
        long grandTotal = liabilityTotal + equityTotal;
        
        // 부채와 자본총계
        gridData.add(createGridRow("", "", "", "", "부채와 자본총계", "", 
            formatAmount(grandTotal), false, 0, true));
    }
    
    /**
     * 기본 구조 추가 (오류 시)
     */
    private void addDefaultStructure(List<Map<String, Object>> gridData) {
        gridData.add(createGridRow("자 산", "", "", "", "", "", "", true, 0, false));
        gridData.add(createGridRow("부 채", "", "", "", "", "", "", true, 0, false));
        gridData.add(createGridRow("자 본", "", "", "", "", "", "", true, 0, false));
    }
    
    /**
     * 그리드 행 생성
     */
    private Map<String, Object> createGridRow(String section, String subsection, String subcategory,
            String accountCode, String accountName, String amount, String total, 
            boolean isHeader, int level, boolean isGrandTotal) {
        
        Map<String, Object> row = new HashMap<>();
        row.put("section", section);
        row.put("subsection", subsection);
        row.put("subcategory", subcategory);
        row.put("accountCode", accountCode);
        row.put("accountName", accountName);
        row.put("amount", amount);
        row.put("total", total);
        row.put("isHeader", isHeader);
        row.put("level", level);
        row.put("isGrandTotal", isGrandTotal);
        
        return row;
    }
    
    /**
     * 금액 포맷팅
     */
    private String formatAmount(Long amount) {
        if (amount == null || amount == 0) {
            return "";
        }
        return String.format("%,d", amount);
    }
    
    /**
     * 총계 계산
     */
    private long calculateTotal(List<BalanceSheetDTO> dataList) {
        if (dataList == null) return 0;
        return dataList.stream()
            .mapToLong(dto -> dto.getSumAmount() != null ? dto.getSumAmount() : 0)
            .sum();
    }
}
