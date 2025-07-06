package com.eflix.acc.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import com.eflix.acc.dto.IncomeStatementDTO;

/* ============================================
  - 작성자   : 김희정
  - 최초작성 : 2025-07-05
  - 설명     : IncomeStatementMapper interface
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-07-05 (김희정): interface 생성
=============================================== */
@Mapper
public interface IncomeStatementMapper {
  List<IncomeStatementDTO> getIncomeStatementByYear(Map<String, Object> params);

    /**
     * 상품 당기매입액 조회
     */
    Long getGoodsPurchaseAmount(Map<String, Object> params);
    
    /**
     * 상품 기말재고액 조회
     */
    Long getGoodsEndingInventory(Map<String, Object> params);
    
    /**
     * 상품 기초재고액 조회 (전년도 기말재고액)
     */
    Long getGoodsBeginningInventory(Map<String, Object> params);
    
    /**
     * 상품 타계정대체액 조회
     */
    Long getGoodsTransferAmount(Map<String, Object> params);
}
