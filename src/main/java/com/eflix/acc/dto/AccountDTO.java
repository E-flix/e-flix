package com.eflix.acc.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* ============================================
  - 작성자   : 김희정
  - 최초작성 : 2025-06-19
  - 설명     : 계정과목DTO
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김희정): DTO 임시 생성 
  - 2025-06-20 (김희정): DTO 데이터타입 설정 
=============================================== */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
  private int accountCode;
  private String accountName;
  private String accountType;
  private String standardAccountName;
  private String standardEnglishName;
  private String majorCategory;
  private String middleCategory;
  private String minorCategory;
  private String useFlag;
  private Date createdAt;
  private Date updatedAt;
  private String remarks;
  private String coIdx;
}
