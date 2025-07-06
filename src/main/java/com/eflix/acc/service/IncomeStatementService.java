package com.eflix.acc.service;

import java.util.List;
import java.util.Map;

import com.eflix.acc.dto.IncomeStatementDTO;

/* ============================================
  - 작성자   : 김희정
  - 최초작성 : 2025-07-05
  - 설명     : IncomeStatementService interface
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-07-05 (김희정): service 생성
=============================================== */
public interface IncomeStatementService {
    
    /**
     * 년도별 손익계산서 데이터 조회
     */
    List<IncomeStatementDTO> getIncomeStatementByYear(Map<String, Object> params);
    
    /**
     * 그리드 형식으로 변환
     */
    List<Map<String, Object>> convertToGridFormat(List<IncomeStatementDTO> dbData);
}
