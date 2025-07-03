package com.eflix.acc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* ============================================
  - 작성자   : 김희정
  - 최초작성 : 2025-06-20
  - 설명     : 거래처번호_마스터DTO
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-20 (김희정): DTO 생성 
=============================================== */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartnerNumberMasterDTO {
  private int partnerCode;
  private String partnerType;
}
