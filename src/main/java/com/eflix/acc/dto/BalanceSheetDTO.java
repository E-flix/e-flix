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
  private String standardAccountName; // 표준계정과목명
  private String majorCategory; // 대분류 (자산/부채/자본)
  private String middleCategory; // 중분류
  private String minorCategory; // 소분류
  private Long sumAmount; // 합계 금액
}
