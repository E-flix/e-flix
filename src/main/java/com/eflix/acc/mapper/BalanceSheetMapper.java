package com.eflix.acc.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import com.eflix.acc.dto.BalanceSheetDTO;

/* ============================================
  - 작성자   : 김희정
  - 최초작성 : 2025-07-05
  - 설명     : BalanceSheetMapper interface
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-07-05 (김희정): interface 생성
=============================================== */
@Mapper
public interface BalanceSheetMapper {
  List<BalanceSheetDTO> getBalanceSheetByYear(Map<String, Object> params);
}
