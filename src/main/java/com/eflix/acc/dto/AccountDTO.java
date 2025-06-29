package com.eflix.acc.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;

/* ============================================
  - 작성자   : 김희정
  - 최초작성 : 2025-06-19
  - 설명     : 계정과목DTO
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김희정): DTO 임시 생성 
  - 2025-06-20 (김희정): DTO 데이터타입 설정 
=============================================== */

@Getter
@AllArgsConstructor
public class AccountDTO {
  private int account_code;
  private String account_name;
  private String account_type;
  private String standard_account_name;
  private String standard_english_name;
  private String major_category;
  private String middle_category;
  private String minor_category;
  private String use_flag;
  private Date created_at;
  private Date updated_at;
  private String remarks;
}
