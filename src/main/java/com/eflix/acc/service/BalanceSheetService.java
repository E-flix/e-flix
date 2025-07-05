package com.eflix.acc.service;

import java.util.List;
import java.util.Map;
import com.eflix.acc.dto.BalanceSheetDTO;

/* ============================================
  - 작성자   : 김희정
  - 최초작성 : 2025-07-05
  - 설명     : BalanceSheetService interface
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-07-05 (김희정): service 인터페이스 생성
=============================================== */
public interface BalanceSheetService {
    /**
     * 연도별 재무상태표 데이터 조회
     * @param params 조회 조건 (year, coIdx, month 등)
     * @return 재무상태표 데이터 리스트
     */
    List<BalanceSheetDTO> getBalanceSheetByYear(Map<String, Object> params);
    
    /**
     * 재무상태표 데이터를 그리드 형식으로 변환
     * @param dbData DB에서 조회한 원시 데이터
     * @return 그리드에서 사용할 형식의 데이터
     */
    List<Map<String, Object>> convertToGridFormat(List<BalanceSheetDTO> dbData);
}
