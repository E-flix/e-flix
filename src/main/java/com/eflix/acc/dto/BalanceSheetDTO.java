package com.eflix.acc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/* ============================================
  - 작성자   : 김희정
  - 최초작성 : 2025-07-05
  - 설명     : 재무상태표 DTO
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-07-05 (김희정): DTO 생성
=============================================== */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceSheetDTO {
  private int accountCode;
}
